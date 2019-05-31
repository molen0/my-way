package com.adinnet.service.Impl;

import com.adinnet.dao.AttachMapper;
import com.adinnet.dao.DoctorMapper;
import com.adinnet.dao.ExperienceShareMapper;
import com.adinnet.repository.*;
import com.adinnet.service.ExperienceShareService;
import com.adinnet.utils.FileSizeUtils;
import com.adinnet.utils.PDF2IMAGE;
import com.adinnet.utils.ReturnUtil;
import com.adinnet.utils.UuidUtil;
import com.adinnet.utils.qiniu.QiniuUtils;
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
import java.io.IOException;
import java.util.*;

/**
 * Created by Administrator on 2018/10/9.
 */
@Service
public class ExperienceShareServiceImpl implements ExperienceShareService{

    @Autowired
    private ExperienceShareMapper experienceShareMapper;

    @Autowired
    private DoctorMapper doctorMapper;

    @Autowired
    private AttachMapper attachMapper;

    @Override
    public Page<ExperienceShare> pageList(ExperienceShare experienceShare) {
        Sort sort = new Sort(Sort.Direction.DESC, "updateTime");
        Pageable pageable=new PageRequest(experienceShare.page(), experienceShare.getLimit(),sort);  //分页信息
        Specification<ExperienceShare> spec = new Specification<ExperienceShare>() {        //查询条件构造
            @Override
            public Predicate toPredicate(Root<ExperienceShare> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<Predicate>();
                if(null != experienceShare.getTitle()){
                    Path<String> name = root.get("title");
                    predicates.add(cb.like(name, "%"+experienceShare.getTitle()+"%"));
                }
                if(null != experienceShare.getType()){
                    Path<String> name = root.get("type");
                    predicates.add(cb.equal(name, experienceShare.getType()));
                }
                return cb.and(predicates
                        .toArray(new Predicate[] {}));
            }
        };
        return experienceShareMapper.findAll(spec,pageable);
    }

    @Override
    public ExperienceShare getOne(Integer id) {
        return experienceShareMapper.queryById(id);
    }

    @Override
    public void del(Integer id) {
        experienceShareMapper.deleteById(id);
    }

    @Override
    public Map queryExperienceShare(Integer doctorId) {
        Map map = new HashMap<>();
        Doctor doctor = doctorMapper.queryById(doctorId);
        if(doctor ==null){
            map.put("code","-1");
        }else{
            //政府部门
            List<ExperienceShare> listOne = experienceShareMapper.findByType(1);
            //医疗机构
            List<ExperienceShare> listTwo = experienceShareMapper.findByType(2);
            //居民经验
            List<ExperienceShare> listThree = experienceShareMapper.findByType(3);
            map.put("listOne",listOne);
            map.put("listTwo",listTwo);
            map.put("listThree",listThree);
        }
        return map;
    }

    @Override
    public Map getListByType(Integer doctorId, Integer type) {
        Map map = new HashMap<>();
        Doctor doctor = doctorMapper.queryById(doctorId);
        if(doctor ==null){
            map.put("code","-1");
        }else{
            List<ExperienceShare> list = experienceShareMapper.findMoreByType(type);
            map.put("list",list);
        }
        return map;
    }

    @Override
    public Map getDetail(Integer doctorId, Integer id) {
        Map map = new HashMap<>();
        Doctor doctor = doctorMapper.queryById(doctorId);
        if(doctor ==null){
            map.put("code","-1");
        }else{
            ExperienceShare experienceShare = experienceShareMapper.queryById(id);
            if(experienceShare.getFileType()!=null && experienceShare.getFileType() == 2 && experienceShare.getAttachId() != null){//文档
               List<Attach> list =  attachMapper.findByAttachId(experienceShare.getAttachId());
                map.put("attachList",list);
            }
            map.put("experienceShare",experienceShare);
        }
        return map;
    }

    @Override
    public ModelMap saveExperienceShare(ExperienceShare experienceShare, MultipartFile file,MultipartFile photo) throws IOException {
        if(file != null){
            int flag = judgeFile(file);
            if(flag == 1){
                return ReturnUtil.Error("文件不能大于100M", null, null);
            }else if(flag == 2){
                return ReturnUtil.Error("请上传pdf或视频", null, null);
            }
            String filePath = QiniuUtils.upload(file, "experienceShare");
            experienceShare.setFilePath(filePath);
            experienceShare.setFileName(file.getOriginalFilename()!=null?file.getOriginalFilename():"");
            if(file.getContentType().contains("pdf")){
                String random = UuidUtil.createRandomNo();//生成随机数
                doSaveAttach(file,filePath,random);
                experienceShare.setAttachId(random);
                experienceShare.setFileType(2);
            }else{
                experienceShare.setFileType(1);
            }
            if(photo != null){
                int photoFlag = judgePhotoFile(photo);
                if(photoFlag == 1){
                    return ReturnUtil.Error("请上传图片文件", null, null);
                }
                String photoPath = QiniuUtils.upload(photo,"experiencePhoto");
                experienceShare.setPhoto(photoPath);
            }

            experienceShare.setCreateTime(new Date());
            experienceShare.setUpdateTime(new Date());
            experienceShareMapper.save(experienceShare);
        }
        return ReturnUtil.Success("操作成功", null, "/api/experienceShare/index");
    }



    @Override
    public ModelMap updateExperienceShare(ExperienceShare experienceShare, MultipartFile file,MultipartFile photo) throws IOException {
        if(null != file && file.getOriginalFilename() !=null &&!"".equals(file.getOriginalFilename())){
            int flag = judgeFile(file);
            if(flag == 1){
                return ReturnUtil.Error("文件不能大于100M", null, null);
            }else if(flag == 2){
                return ReturnUtil.Error("请上传pdf或视频", null, null);
            }
            String filePath = QiniuUtils.upload(file, "experienceShare");
            experienceShare.setFilePath(filePath);
            experienceShare.setFileName(file.getOriginalFilename()!=null?file.getOriginalFilename():"");
            if(file.getContentType().contains("pdf")){
                String random = UuidUtil.createRandomNo();//生成随机数
                doSaveAttach(file,filePath,random);
                experienceShare.setAttachId(random);
                experienceShare.setFileType(2);
            }else{
                experienceShare.setFileType(1);
            }

        }else{
            ExperienceShare origial = experienceShareMapper.queryById(experienceShare.getId());
            experienceShare.setFilePath(origial.getFilePath());
            experienceShare.setAttachId(origial.getAttachId());
            experienceShare.setFileType(origial.getFileType());
            experienceShare.setFileName(origial.getFileName());
        }
        if(null != photo && photo.getOriginalFilename() !=null &&!"".equals(photo.getOriginalFilename())){
            int photoFlag = judgePhotoFile(photo);
            if(photoFlag == 1){
                return ReturnUtil.Error("请上传图片文件", null, null);
            }
            String photoPath = QiniuUtils.upload(photo,"experiencePhoto");
            experienceShare.setPhoto(photoPath);
        }else{
            ExperienceShare origial = experienceShareMapper.queryById(experienceShare.getId());
            experienceShare.setPhoto(origial.getPhoto());
        }
        experienceShare.setUpdateTime(new Date());
        experienceShareMapper.saveAndFlush(experienceShare);
        return ReturnUtil.Success("操作成功", null, "/api/experienceShare/index");
    }

    private int judgePhotoFile(MultipartFile photo) {
        int flag = 0;
        String type = photo.getContentType();
        if(!type.contains("image")){
            flag = 1;
        }
        return flag;
    }

    private int judgeFile(MultipartFile file) {
        int flag = 0;
        String type =  file.getContentType();
        if(!type.contains("video") && !type.contains("pdf")){ //判断文件类型
            flag = 2;
        }
        double size = FileSizeUtils.getFileSize(file);
        if(size>1024*100){ //判断文件大小
            flag = 1;
        }
        return flag;
    }

    private void doSaveAttach(MultipartFile file, String filePath,String random) {
        List<String> list = PDF2IMAGE.pdf2Image2(file, filePath);
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
    }
}
