package com.adinnet.service.Impl;

import com.adinnet.common.status.IsDeleted;
import com.adinnet.dao.AttachMapper;
import com.adinnet.dao.CourseCoursewareMapper;
import com.adinnet.dao.CourseMapper;
import com.adinnet.ex.BuzEx;
import com.adinnet.repository.*;
import com.adinnet.repository.dto.CourseCoursewareDto;
import com.adinnet.response.code.CCode;
import com.adinnet.service.CourseCoursewareService;
import com.adinnet.utils.FileSizeUtils;
import com.adinnet.utils.PDF2IMAGE;
import com.adinnet.utils.ReturnUtil;
import com.adinnet.utils.UuidUtil;
import com.adinnet.utils.qiniu.QiniuUtils;
import com.alibaba.druid.util.StringUtils;
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
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by wangx on 2018/9/21.
 */
@Service
@Slf4j
public class CourseCoursewareServiceImpl implements CourseCoursewareService {

    @Autowired
    private CourseCoursewareMapper courseCoursewareMapper;

    @Autowired
    private AttachMapper attachMapper;
    @Autowired
    private CourseMapper courseMapper;

    @Override
    public Page<List<Map<String, Object>>> pageList(CourseCoursewareDto courseCoursewareDto) {
        Sort sort = new Sort(Sort.Direction.DESC, "update_time");
        Pageable pageable = new PageRequest(courseCoursewareDto.page(), courseCoursewareDto.getLimit(), sort);  //分页信息
       String courseName = courseCoursewareDto.getCourseName().trim();
        return courseCoursewareMapper.queryAllCourseware( courseName, pageable);
    }

    @Override
    public PageBean<CourseCourseware> pageList3(CourseCourseware courseCourseware) {
        PageBean<CourseCourseware> page = new PageBean<>();
        String hql = " SELECT em.specialistName, co.*,c.course_attr AS courseAttr,c.title as title " +
                " FROM tb_course_courseware co LEFT JOIN tb_course c ON c.id = co.course_id " +
                " LEFT JOIN (SELECT GROUP_CONCAT(s.`name`) `specialistName`,cs.course_id FROM " +
                " tb_course_specialist cs LEFT JOIN tb_specialist s ON s.id = cs.specialist_id " +
                " WHERE s.is_delete = 1 GROUP BY cs.course_id " +
                " ) em ON em.course_id = co.course_id WHERE co.is_deleted = 1 ";
        String countHql = " SELECT COUNT(1) " +
                " FROM tb_course_courseware co LEFT JOIN tb_course c ON c.id = co.course_id " +
                " LEFT JOIN (SELECT GROUP_CONCAT(s.`name`) `specialistName`,cs.course_id FROM " +
                " tb_course_specialist cs LEFT JOIN tb_specialist s ON s.id = cs.specialist_id " +
                " WHERE s.is_delete = 1 GROUP BY cs.course_id " +
                " ) em ON em.course_id = co.course_id WHERE co.is_deleted = 1 ";
        StringBuffer str = new StringBuffer(hql);
        StringBuffer str1 = new StringBuffer(countHql);
        if(null != courseCourseware){
            if(null != courseCourseware.getName() && !"".equals(courseCourseware.getName())){
                str.append("  and co.name like concat('%','"+courseCourseware.getName()+"','%') ");
                str1.append("  and co.name   like concat('%','"+courseCourseware.getName()+"','%') ");
            }
            if(null != courseCourseware.getCourseAttr() && !"".equals(courseCourseware.getCourseAttr())){
                str.append("  and c.course_attr ="+courseCourseware.getCourseAttr()+" ");
                str1.append("   and c.course_attr ="+courseCourseware.getCourseAttr()+" ");
            }
        }
        page.setHql(str.toString());
        page.setCountHql(str1.toString());
        page.setSortName("co.update_time desc");
        page.setPageNum(courseCourseware.getOffset());
        page.setPageSize(courseCourseware.getLimit());
        page = courseCoursewareMapper.findNatPage(page);
        List<CourseCourseware>  courseCoursewares = page.getDatas();

        for(CourseCourseware courseOut: courseCoursewares){
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
    public JSONObject findByCourseId(Integer courseId) {
        List<CourseCourseware> list = courseCoursewareMapper.findByCourseId(courseId);
        JSONObject j = new JSONObject();
        List<Attach> attach = new ArrayList<>();
        if (list.size() > 0) {
            String attachId = list.get(0).getAttachId();
               if(attachId ==null){
                  log.error("attachId is null");
                   throw new BuzEx(CCode. C_PARAM_IS_NULL);
               }
               attach = attachMapper.findByAttachId(attachId);

        }
        if(attach.size()<1){
            attach = new ArrayList<>();
           // Attach ah = new Attach();
          //  attach.add(ah);

        }
        j.put("attach", attach);
        return j;
    }

    @Override
    public CourseCourseware getOne(Integer id) {
        if (null == id) {
            log.error("coursewareId is null");
            throw new BuzEx(CCode.C_PARAM_IS_NULL);
        }
        return courseCoursewareMapper.getOne(id);
    }

    @Override
    public ModelMap save(CourseCourseware courseCourseware, MultipartFile file) throws IOException {
        if ( null!=file ) {
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

            CourseCourseware clr = null;
            if (null == courseCourseware) {
                log.error("courseLearningResource is null");
                throw new BuzEx(CCode.C_PARAM_IS_NULL);
            }
            courseCourseware.setPdfPath(pdfPath);
            courseCourseware.setIsDeleted(IsDeleted._1.getCode());
            String  fileName =  file.getOriginalFilename();
            courseCourseware.setName(fileName);
            courseCourseware.setAttachId(random);
            clr = courseCoursewareMapper.save(courseCourseware);
            if (list != null && list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    Attach attach = new Attach();
                    String path = list.get(i);
                    attach.setPath(path);
                    attach.setAttachId(random);
                    attach.setType(3);
                    attach.setFileType(0);
                    attachMapper.save(attach);
                }
            }

            return ReturnUtil.Success("操作成功", null, "/api/courseCourseware/index");
        }
        return ReturnUtil.Error("操作失败", null, null);
    }

    @Override
    @Transactional
    public ModelMap update(CourseCourseware courseCourseware, MultipartFile file) throws IOException {
        if( null!= file) {
            Integer flag = judgeFile(file);
            if (flag == 1) {
                return ReturnUtil.Error("文件不能大于10M", null, null);
            } else if (flag == 2) {
                return ReturnUtil.Error("请上传pdf格式的文件", null, null);
            }else if (flag == 3){
                return ReturnUtil.Error("文件内容不能为空", null, null);
            }
            int resourceId = courseCourseware.getId();
            CourseCourseware resource = courseCoursewareMapper.getOne(resourceId);
            Integer result = null;

            try {
                if (null != resource) {
                    Integer courseId = courseCourseware.getCourseId();
                    String pdfPath = QiniuUtils.upload(file, "pdfPath");
                    String  fileName =  file.getOriginalFilename();
                    if (null != courseId && null != pdfPath  && null != fileName ) {

                        result = courseCoursewareMapper.updateById(courseId, pdfPath,fileName ,resourceId);

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


            return ReturnUtil.Success("操作成功", null, "/api/courseCourseware/index");
        }else{
            Integer courseId = courseCourseware.getCourseId();
            int resourceId = courseCourseware.getId();
          Integer  result = courseCoursewareMapper.updateById2(courseId,resourceId);
          if (result==1){
              return ReturnUtil.Success("操作成功", null, "/api/courseCourseware/index");
          }
        }
        return ReturnUtil.Error("操作失败", null, null);
    }

    @Override
    @Transactional
    public Integer deleteById(Integer id) {

        if (null == id) {
            log.error("coursewareId is null");
            throw new BuzEx(CCode.C_PARAM_IS_NULL);
        }
        return courseCoursewareMapper.delById(id);
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
