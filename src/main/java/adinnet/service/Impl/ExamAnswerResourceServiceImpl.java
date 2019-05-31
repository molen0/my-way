package adinnet.service.Impl;

import com.adinnet.common.status.IsDeleted;
import com.adinnet.dao.AttachMapper;
import com.adinnet.dao.CourseCoursewareMapper;
import com.adinnet.dao.CourseMapper;
import com.adinnet.dao.ExamAnswerResourceMapper;
import com.adinnet.ex.BuzEx;
import com.adinnet.http.HttpUtils;
import com.adinnet.repository.*;
import com.adinnet.repository.dto.ExamAnswerResourceDto;
import com.adinnet.response.code.CCode;
import com.adinnet.service.ExamAnswerResourceService;
import com.adinnet.utils.FileSizeUtils;
import com.adinnet.utils.PDF2IMAGE;
import com.adinnet.utils.ReturnUtil;
import com.adinnet.utils.UuidUtil;
import com.adinnet.utils.qiniu.QiniuUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

import org.apache.http.HttpException;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/10/9.
 */
@Service
@Slf4j
public class ExamAnswerResourceServiceImpl implements ExamAnswerResourceService {

    @Autowired
    private ExamAnswerResourceMapper examAnswerResourceMapper;

    @Autowired
    private AttachMapper attachMapper;

    @Autowired
    private CourseMapper courseMapper;
    @Autowired
    private CourseCoursewareMapper courseCoursewareMapper;
    @Override
    public Page<List<Map<String, Object>>> pageList(ExamAnswerResourceDto examAnswerResourceDto) {
        Sort sort = new Sort(Sort.Direction.DESC, "create_time");
        Pageable pageable = new PageRequest(examAnswerResourceDto.page(), examAnswerResourceDto.getLimit(), sort);  //分页信息
        String courseName = examAnswerResourceDto.getCourseName().trim();
        return examAnswerResourceMapper.queryAllResource(courseName,pageable);
    }

    @Override
    public PageBean<ExamAnswerResource> pageList3(ExamAnswerResource examAnswerResource) {
        PageBean<ExamAnswerResource> page = new PageBean<>();
        String hql = " SELECT em.specialistName, co.*,c.course_attr AS courseAttr,c.title as title " +
                " FROM  tb_exam_answer_resource co LEFT JOIN tb_course c ON c.id = co.course_id " +
                " LEFT JOIN (SELECT GROUP_CONCAT(s.`name`) `specialistName`,cs.course_id FROM " +
                " tb_course_specialist cs LEFT JOIN tb_specialist s ON s.id = cs.specialist_id " +
                " WHERE s.is_delete = 1 GROUP BY cs.course_id " +
                " ) em ON em.course_id = co.course_id WHERE co.is_deleted = 1 ";
        String countHql = "SELECT COUNT(1) " +
                " FROM tb_exam_answer_resource co LEFT JOIN tb_course c ON c.id = co.course_id " +
                " LEFT JOIN (SELECT GROUP_CONCAT(s.`name`) `specialistName`,cs.course_id FROM " +
                " tb_course_specialist cs LEFT JOIN tb_specialist s ON s.id = cs.specialist_id " +
                " WHERE s.is_delete = 1 GROUP BY cs.course_id " +
                " ) em ON em.course_id = co.course_id WHERE co.is_deleted = 1 ";
        StringBuffer str = new StringBuffer(hql);
        StringBuffer str1 = new StringBuffer(countHql);
        if(null != examAnswerResource){
            if(null != examAnswerResource.getCourseName() && !"".equals(examAnswerResource.getCourseName())){
                str.append("  and c.title like concat('%','"+examAnswerResource.getCourseName()+"','%') ");
                str1.append("  and c.title  like concat('%','"+examAnswerResource.getCourseName()+"','%') ");
            }
            if(null != examAnswerResource.getCourseAttr() && !"".equals(examAnswerResource.getCourseAttr())){
                str.append("  and c.course_attr ="+examAnswerResource.getCourseAttr()+" ");
                str1.append("   and c.course_attr ="+examAnswerResource.getCourseAttr()+" ");
            }
        }
        page.setHql(str.toString());
        page.setCountHql(str1.toString());
        page.setSortName("co.update_time desc");
        page.setPageNum(examAnswerResource.getOffset());
        page.setPageSize(examAnswerResource.getLimit());
        page = examAnswerResourceMapper.findNatPage(page);
        List<ExamAnswerResource>  examAnswerResources = page.getDatas();

        for(ExamAnswerResource courseOut: examAnswerResources){
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
    public ExamAnswerResource getOne(Integer id) {
        if (null == id) {
            log.error("resourceId is null");
            throw new BuzEx(CCode.C_PARAM_IS_NULL);
        }
        return examAnswerResourceMapper.getOne(id);
    }

    @Override
    public ModelMap save(ExamAnswerResource examAnswerResource, MultipartFile file, MultipartFile videoFile) throws IOException, HttpException {
        if (videoFile == null && file == null) {
            return ReturnUtil.Error("请上传视频或PDF", null, null);
        }

        if (videoFile != null) {
            int flag = judgeFile(videoFile);
            if (flag == 1) {
                return ReturnUtil.Error("视频不能大于100M", null, null);
            } else if (flag == 2) {
                return ReturnUtil.Error("请上传视频文件", null, null);
            } else if (flag == 3) {
                return ReturnUtil.Error("文件内容不能为空", null, null);
            }
            String videoPath = QiniuUtils.upload(videoFile, "ExamAnswerVideo");
            // 根据响应获取文件大小
            String result = HttpUtils.httpGet(videoPath + "?avinfo", "utf-8");
            JSONObject streams = JSONObject.parseObject(result);
            JSONArray jsonArray = streams.getJSONArray("streams");
            String duration = jsonArray.getJSONObject(0).getString("duration");
            double temp = Math.floor(Double.parseDouble(duration));
            String videoTime = String.valueOf((int) temp);
            examAnswerResource.setVideoPath(videoPath);
            examAnswerResource.setVideoTime(videoTime);
            examAnswerResource.setCreateTime(new Date());
            examAnswerResource.setUpdateTime(new Date());

            if (file != null) {
                Integer flag2 = judgeFile2(file);
                if (flag2 == 1) {
                    return ReturnUtil.Error("文件不能大于10M", null, null);
                } else if (flag2 == 2) {
                    return ReturnUtil.Error("请上传pdf格式的文件", null, null);
                } else if (flag2 == 3) {
                    return ReturnUtil.Error("文件内容不能为空", null, null);
                }
                String pdfPath = QiniuUtils.upload(file, "ExamAnswerPdfPath");//pdf上传返回路径
                String random = UuidUtil.createRandomNo();//生成随机数

                List<String> list = PDF2IMAGE.pdf2Image2(file, pdfPath);

                ExamAnswerResource clr = null;
                if (null == examAnswerResource) {
                    log.error("examAnswerResource is null");
                    throw new BuzEx(CCode.C_PARAM_IS_NULL);
                }
                examAnswerResource.setPdfPath(pdfPath);
                examAnswerResource.setIsDeleted(IsDeleted._1.getCode());
                examAnswerResource.setAttachId(random);
                String pdfName = file.getOriginalFilename();
                examAnswerResource.setPdfName(pdfName);
                clr = examAnswerResourceMapper.save(examAnswerResource);
                List<Attach> list1 = new ArrayList<>();
                if (list != null && list.size() > 0) {
                    for (int i = 0; i < list.size(); i++) {
                        Attach attach = new Attach();
                        String path = list.get(i);
                        attach.setPath(path);
                        attach.setAttachId(random);
                        attach.setType(3);
                        attach.setFileType(0);
                        list1.add(attach);
                        //   attachMapper.save(attach);
                    }
                    attachMapper.saveAll(list1);
                    return ReturnUtil.Success("操作成功", null, "/api/answer/index");
                }

            } else {
                String random = UuidUtil.createRandomNo();//生成随机数
                examAnswerResource.setIsDeleted(IsDeleted._1.getCode());
                examAnswerResource.setAttachId(random);
                examAnswerResourceMapper.save(examAnswerResource);
                return ReturnUtil.Success("操作成功", null, "/api/answer/index");
            }

        } else {
            if (file != null) {
                Integer flag2 = judgeFile2(file);
                if (flag2 == 1) {
                    return ReturnUtil.Error("文件不能大于10M", null, null);
                } else if (flag2 == 2) {
                    return ReturnUtil.Error("请上传pdf格式的文件", null, null);
                } else if (flag2 == 3) {
                    return ReturnUtil.Error("文件内容不能为空", null, null);
                }
                String pdfPath = QiniuUtils.upload(file, "ExamAnswerPdfPath");//pdf上传返回路径
                String random = UuidUtil.createRandomNo();//生成随机数

                List<String> list = PDF2IMAGE.pdf2Image2(file, pdfPath);

                ExamAnswerResource clr = null;
                if (null == examAnswerResource) {
                    log.error("examAnswerResource is null");
                    throw new BuzEx(CCode.C_PARAM_IS_NULL);
                }
                examAnswerResource.setCreateTime(new Date());
                examAnswerResource.setUpdateTime(new Date());
                examAnswerResource.setPdfPath(pdfPath);
                examAnswerResource.setIsDeleted(IsDeleted._1.getCode());
                examAnswerResource.setAttachId(random);
                String pdfName = file.getOriginalFilename();
                examAnswerResource.setPdfName(pdfName);
                clr = examAnswerResourceMapper.save(examAnswerResource);
                List<Attach> list1 = new ArrayList<>();
                if (list != null && list.size() > 0) {
                    for (int i = 0; i < list.size(); i++) {
                        Attach attach = new Attach();
                        String path = list.get(i);
                        attach.setPath(path);
                        attach.setAttachId(random);
                        attach.setType(3);
                        attach.setFileType(0);
                        list1.add(attach);
                        //   attachMapper.save(attach);
                    }
                    attachMapper.saveAll(list1);
                    return ReturnUtil.Success("操作成功", null, "/api/answer/index");
                }
            }


        }
        return ReturnUtil.Error("操作失败", null, null);
    }
    @Override
    @Transactional
    public ModelMap update(ExamAnswerResource examAnswerResource, MultipartFile file, MultipartFile videoFile) throws IOException, HttpException {
        if(null !=videoFile&&videoFile.getOriginalFilename() !=null &&!"".equals(videoFile.getOriginalFilename())){
            int flag =judgeFile(videoFile);
            if(flag == 1){
                return ReturnUtil.Error("视频不能大于100M", null, null);
            }else if(flag == 2){
                return ReturnUtil.Error("请上传视频文件", null, null);
            }else if (flag == 3){
                return ReturnUtil.Error("文件内容不能为空", null, null);
            }
            String videoPath = QiniuUtils.upload(videoFile, "courseVideo");
            // 根据响应获取文件大小
            String result = HttpUtils.httpGet(videoPath + "?avinfo", "utf-8");
            JSONObject streams = JSONObject.parseObject(result);
            JSONArray jsonArray = streams.getJSONArray("streams");
            String duration = jsonArray.getJSONObject(0).getString("duration");
            double temp =  Math.floor(Double.parseDouble(duration));
            String videoTime = String.valueOf((int)temp);
            examAnswerResource.setVideoPath(videoPath);
            examAnswerResource.setVideoTime(videoTime);

        }else{
            ExamAnswerResource original = examAnswerResourceMapper.queryById(examAnswerResource.getId());
            examAnswerResource.setVideoTime(original.getVideoTime());
            examAnswerResource.setVideoPath(original.getVideoPath());
        }
        examAnswerResource.setUpdateTime(new Date());
        Integer result = null;
        if (null != file &&file.getOriginalFilename() !=null &&!"".equals(file.getOriginalFilename())) {
            Integer flag = judgeFile2(file);
            if (flag == 1) {
                return ReturnUtil.Error("文件不能大于10M", null, null);
            } else if (flag == 2) {
                return ReturnUtil.Error("请上传pdf格式的文件", null, null);
            }else if (flag == 3){
                return ReturnUtil.Error("文件内容不能为空", null, null);
            }
            String   pdfName=file.getOriginalFilename();
            examAnswerResource.setPdfName(pdfName);
            int resourceId = examAnswerResource.getId();
            ExamAnswerResource  resource = examAnswerResourceMapper.getOne(resourceId);


                if (null != resource) {
                    Integer courseId = examAnswerResource.getCourseId();
                    String pdfPath = QiniuUtils.upload(file, "pdfPath");

                    if (null != courseId  && null != pdfPath) {

                    result = examAnswerResourceMapper.updateById(courseId, examAnswerResource.getVideoPath(),examAnswerResource.getVideoTime(), pdfPath,examAnswerResource.getPdfName() ,resourceId);
                        String attachId = resource.getAttachId();
                        if (null != attachId && result>0 ) {
                            List<String> list = PDF2IMAGE.pdf2Image2(file, pdfPath);
                           attachMapper.delByAttachId(attachId);


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

                                    return ReturnUtil.Success("操作成功", null, "/api/answer/index");
                                    }

                            }
                        }
                    }
                }else {

            int resourceId = examAnswerResource.getId();
            ExamAnswerResource  resource = examAnswerResourceMapper.getOne(resourceId);
            if (null != resource && null !=resource.getPdfPath()) {
                Integer courseId = examAnswerResource.getCourseId();
                result = examAnswerResourceMapper.updateById(courseId, examAnswerResource.getVideoPath(), examAnswerResource.getVideoTime(), resource.getPdfPath(), examAnswerResource.getPdfName(), examAnswerResource.getId());
            }else{
                Integer courseId = examAnswerResource.getCourseId();
                result = examAnswerResourceMapper.updateById2(courseId, examAnswerResource.getVideoPath(), examAnswerResource.getVideoTime(), examAnswerResource.getId());
            }
            if (result>0) {
                return ReturnUtil.Success("操作成功", null, "/api/answer/index");
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
        return examAnswerResourceMapper.delById(id);
    }

    @Override
    public JSONObject findByCourseId(Integer courseId) {

        if (null ==courseId){
            log.error("needsId is null");
            throw new BuzEx (CCode.C_PARAM_IS_NULL);
        }
        JSONObject j = new JSONObject();
        List<Attach> attach =new ArrayList<>();
        List<ExamAnswerResource>  resource =  examAnswerResourceMapper.findByCourseId(courseId);
        if (resource.size()>0) {
            for (int i = 0; i <resource.size();i++){

                Course course =courseMapper.queryById(courseId);
                if (null !=course) {
                    ExamAnswerResource examAnswerResource = resource.get(i);
                    examAnswerResource.setCourseName(course.getTitle());
                }
                String attachId = resource.get(0).getAttachId();
                if(attachId ==null){
                    log.error("attachId is null");
                    throw new BuzEx(CCode. C_PARAM_IS_NULL);
                }
                attach = attachMapper.findByAttachId(attachId);

            }
        }
            if(attach.size()<1){
                attach = new ArrayList<>();
                // Attach ah = new Attach();
                //  attach.add(ah);

            }
            if (resource ==null ||resource.size()<1){
                resource = new ArrayList<>();
            }
            j.put("resource",resource);
            j.put("attach", attach);

        return j;
    }


    public int judgeFile(MultipartFile file){
        int flag = 0;
        String type =  file.getContentType();
        if(!type.contains("mp4")){
            flag = 2;
            //return ReturnUtil.Error("请上传视频文件", null, null);
        }

        if (file.isEmpty()){
            flag = 3;
        }else {
            double size = FileSizeUtils.getFileSize(file);
            if (size>1024*100) {
                flag = 1;
                //return ReturnUtil.Error("视频不能大于100M", null, null);
            }
        }
        return flag;
    }
    public Integer judgeFile2(MultipartFile file) {
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
