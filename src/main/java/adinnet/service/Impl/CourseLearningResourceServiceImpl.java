package adinnet.service.Impl;

import com.adinnet.common.status.IsDeleted;
import com.adinnet.dao.*;
import com.adinnet.ex.BuzEx;
import com.adinnet.repository.*;

import com.adinnet.repository.dto.CourseEvaluateDto;
import com.adinnet.repository.dto.CourseLearningResourceDto;
import com.adinnet.response.code.CCode;
import com.adinnet.service.CourseLearningResourceService;
import com.adinnet.utils.*;
import com.adinnet.utils.qiniu.QiniuUtils;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by wangx on 2018/9/20.
 */
@Service
@Slf4j
public class CourseLearningResourceServiceImpl implements CourseLearningResourceService {

    @Autowired
    private CourseLearningResourceMapper courseLearningResourceMapper;
    @Autowired
    private AttachMapper attachMapper;
    @Autowired
    private CourseMapper courseMapper;
    @Autowired
    private CourseCoursewareMapper courseCoursewareMapper;


    @Override
    public Page<List<Map<String, Object>>> pageList1(CourseLearningResourceDto courseLearningResourceDto) {
        Sort sort = new Sort(Sort.Direction.DESC, "create_time");
        Pageable pageable = new PageRequest(courseLearningResourceDto.page(), courseLearningResourceDto.getLimit(), sort);  //分页信息
        String name = courseLearningResourceDto.getName().trim();
        return courseLearningResourceMapper.queryAllResource(name,pageable);
    }

    @Override
    public PageBean<CourseLearningResource> pageList3(CourseLearningResource courseLearningResource) {
        PageBean<CourseLearningResource> page = new PageBean<>();
        String hql = " SELECT em.specialistName, co.*,c.course_attr AS courseAttr,c.title as title " +
                " FROM tb_course_learning_resource co LEFT JOIN tb_course c ON c.id = co.course_id " +
                " LEFT JOIN (SELECT GROUP_CONCAT(s.`name`) `specialistName`,cs.course_id FROM " +
                " tb_course_specialist cs LEFT JOIN tb_specialist s ON s.id = cs.specialist_id " +
                " WHERE s.is_delete = 1 GROUP BY cs.course_id " +
                " ) em ON em.course_id = co.course_id WHERE co.is_deleted = 1 ";
        String countHql = " SELECT COUNT(1) " +
                " FROM tb_course_learning_resource co LEFT JOIN tb_course c ON c.id = co.course_id " +
                " LEFT JOIN (SELECT GROUP_CONCAT(s.`name`) `specialistName`,cs.course_id FROM " +
                " tb_course_specialist cs LEFT JOIN tb_specialist s ON s.id = cs.specialist_id " +
                " WHERE s.is_delete = 1 GROUP BY cs.course_id " +
                " ) em ON em.course_id = co.course_id WHERE co.is_deleted = 1 ";
        StringBuffer str = new StringBuffer(hql);
        StringBuffer str1 = new StringBuffer(countHql);
        if(null != courseLearningResource){
            if(null != courseLearningResource.getName() && !"".equals(courseLearningResource.getName())){
                str.append("  and co.name like concat('%','"+courseLearningResource.getName()+"','%') ");
                str1.append("  and co.name   like concat('%','"+courseLearningResource.getName()+"','%') ");
            }
            if(null != courseLearningResource.getCourseAttr() && !"".equals(courseLearningResource.getCourseAttr())){
                str.append("  and c.course_attr ="+courseLearningResource.getCourseAttr()+" ");
                str1.append("   and c.course_attr ="+courseLearningResource.getCourseAttr()+" ");
            }
        }
        page.setHql(str.toString());
        page.setCountHql(str1.toString());
        page.setSortName("co.update_time desc");
        page.setPageNum(courseLearningResource.getOffset());
        page.setPageSize(courseLearningResource.getLimit());
        page = courseLearningResourceMapper.findNatPage(page);
        List<CourseLearningResource>  courseLearningResources = page.getDatas();

        for(CourseLearningResource courseOut: courseLearningResources){
            Integer courseId = courseOut.getCourseId();
            Course course = courseMapper.queryById(courseId);
            if(course != null){
                courseOut.setCourseName(course.getTitle()==null?"":course.getTitle());
                courseOut.setCourseAttr(course.getCourseAttr()==null?null:course.getCourseAttr());
            }
            String specName=   courseCoursewareMapper.querySpecNameByCourseId(courseId);
            courseOut.setSpecialistNames((specName==null? "":specName));
        }
        return page;
    }


    @Override
    public JSONObject queryResourceByCourseId(Integer courseId) {
        JSONObject j = new JSONObject();
        List<CourseLearningResource> courseLearningResource = courseLearningResourceMapper.findAll(courseId);
        if (courseLearningResource.size()< 1) {
            courseLearningResource = new ArrayList<>();
        }
        j.put("courseLearningResource", courseLearningResource);
        return j;
    }

    @Override
    public JSONObject findPdfByphoto(String attachId) {
        JSONObject j = new JSONObject();
        List<Attach> attach = attachMapper.findByAttachId(attachId);
        if (attach.size()<1) {
            attach = new ArrayList<>();
        }
        List<CourseLearningResource> courseLearningResource = courseLearningResourceMapper.findByAttachId(attachId);
        //  List<Object[]> clvo =courseLearningResourceVoMapper.findPdfByphoto(pdfId); //clvo  根据pdfid  查询转化的图片
        if (courseLearningResource.size()<1) {

            courseLearningResource = new ArrayList<>();
            attach = new ArrayList<>();
        }
        j.put("attach", attach);
        j.put("courseLearningResource", courseLearningResource);
        return j;
    }

    @Override
    public CourseLearningResource getOne(Integer id) {
        if (null == id) {
            log.error("resourceId is null");
            throw new BuzEx(CCode.C_PARAM_IS_NULL);
        }
        return courseLearningResourceMapper.getOne(id);
    }

    @Override
    public ModelMap save(CourseLearningResource courseLearningResource, MultipartFile file) throws IOException {
        if (file != null) {

            Integer flag = judgeFile(file);
            if (flag == 1) {
                return ReturnUtil.Error("文件不能大于10M", null, null);
            } else if (flag == 2) {
                return ReturnUtil.Error("请上传pdf格式的文件", null, null);
            }else if (flag == 3){
                return ReturnUtil.Error("文件内容不能为空", null, null);
            }
            String pdfPath = QiniuUtils.upload(file, "pdfPath");//pdf上传返回路径
            String random = UuidUtil.createRandomNo();//生成随机数

            List<String> list = PDF2IMAGE.pdf2Image2(file, pdfPath);

            CourseLearningResource clr = null;
            if (null == courseLearningResource) {
                log.error("courseLearningResource is null");
                throw new BuzEx(CCode.C_PARAM_IS_NULL);
            }
            courseLearningResource.setPdfPath(pdfPath);
            courseLearningResource.setIsDeleted(IsDeleted._1.getCode());
            courseLearningResource.setAttachId(random);
         String  fileName =  file.getOriginalFilename();
            courseLearningResource.setName(fileName);
            clr = courseLearningResourceMapper.save(courseLearningResource);
            List<Attach> list1 = new ArrayList<>();
            if (list != null && list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    Attach attach = new Attach();
                    String path = list.get(i);
                    attach.setPath(path);
                    attach.setAttachId(random);
                    attach.setType(3);
                    attach.setFileType(0);
                   // attachMapper.save(attach);
                    list1.add(attach) ;
                }
                attachMapper.saveAll(list1);
            }
            return ReturnUtil.Success("操作成功", null, "/api/courseLearningResource/index");
        }
        return ReturnUtil.Error("操作失败", null, null);
    }

    @Override
    @Transactional
    public ModelMap update(CourseLearningResource courseLearningResource, MultipartFile file) throws IOException {
        if (null != file) {
            Integer flag = judgeFile(file);
            if (flag == 1) {
                return ReturnUtil.Error("文件不能大于10M", null, null);
            } else if (flag == 2) {
                return ReturnUtil.Error("请上传pdf格式的文件", null, null);
            }else if (flag == 3){
                return ReturnUtil.Error("文件内容不能为空", null, null);
            }
            int resourceId = courseLearningResource.getId();
            CourseLearningResource resource = courseLearningResourceMapper.getOne(resourceId);
            Integer result = null;

            try {
                if (null != resource) {
                    Integer courseId = courseLearningResource.getCourseId();
                    String  fileName =  file.getOriginalFilename();

                    String pdfPath = QiniuUtils.upload(file, "pdfPath");
                    if (null != courseId && null != fileName && null != pdfPath) {

                        result = courseLearningResourceMapper.updateById(courseId, fileName, pdfPath, resourceId);

                        String attachId = resource.getAttachId();
                        if (null != attachId && result > 0) {
                            List<String> list = PDF2IMAGE.pdf2Image2(file, pdfPath);
                            int delResult = attachMapper.delByAttachId(attachId);

                            if (delResult > 0) {
                                if (list != null && list.size() > 0) {
                                    for (int i = 0; i < list.size(); i++) {
                                        Attach attach = new Attach();
                                        String path = list.get(i);
                                        attach.setPath(path);
                                        attach.setAttachId(attachId);
                                        attach.setType(3);
                                        attach.setFileType(0);
                                        attachMapper.save(attach);
                                    }
                                }
                            }
                        }
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
                throw new BuzEx(CCode.C_UPDATE_FAILED);
            }

            return ReturnUtil.Success("操作成功", null, "/api/courseLearningResource/index");
        }else {
            Integer courseId = courseLearningResource.getCourseId();
            int resourceId = courseLearningResource.getId();
          Integer  result = courseLearningResourceMapper.updateById2(courseId,resourceId);
          if (result==1){
              return ReturnUtil.Success("操作成功", null, "/api/courseLearningResource/index");
          }
        }
        return ReturnUtil.Error("操作失败", null, null);
    }

    @Override
    @Transactional
    public Integer deleteById(Integer id) {
        if (null == id) {
            log.error("ResourceId is null");
            throw new BuzEx(CCode.C_PARAM_IS_NULL);
        }
        return courseLearningResourceMapper.delById(id);
    }

    @Override
    public List<Course> queryListAllExists() {
        return courseMapper.queryListAll();
    }


    public Integer judgeFile(MultipartFile file) {
        Integer flag = 0;
        String type = file.getContentType();
        if (!type.contains("pdf")) {
            flag = 2;
            //return ReturnUtil.Error("请上传pdf格式的文件", null, null);
        }
        if (file.isEmpty()){
            flag = 3;
        }else {
            double size = FileSizeUtils.getFileSize(file);
            if (size > 1024 * 10) {
                flag = 1;
                //return ReturnUtil.Error("文件不能大于10M", null, null);
            }
        }
        return flag;
    }
}
