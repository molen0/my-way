package com.adinnet.service.Impl;

import com.adinnet.dao.DoctorAreaMapper;
import com.adinnet.dao.DoctorInfoLogMapper;
import com.adinnet.dao.DoctorMapper;
import com.adinnet.dao.DoctorVisitLogMapper;
import com.adinnet.ex.BuzEx;
import com.adinnet.repository.Doctor;
import com.adinnet.repository.DoctorArea;
import com.adinnet.repository.DoctorInfoLog;
import com.adinnet.repository.DoctorVisitLog;
import com.adinnet.response.code.CCode;
import com.adinnet.service.DoctorService;
import com.adinnet.utils.HttpClientUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.*;

/**
 * Created by adinnet on 2018/9/20.
 */
@Service
@Slf4j
public class DoctorServiceImpl implements DoctorService {

    //万达信息医生信息查询参数
    private static final String WANDA_channelCode = "10002";
    private static final String WANDA_lightAppCode = "10102";
    private static final String WANDA_authKey = "771a832a-0a38-4403-9a55-3aa773e19a61";
    //测试
    //private static final String WANDA_url = "http://218.80.250.99/healthCloud-open-gateway-te/gateway/internalGetDoctorInfo";
    //生产
    //private static final String WANDA_url = "https://www.wdjky.com/healthCloud-open-gateway/gateway/internalGetDoctorInfo";
    //生产内网
    //private static final String WANDA_url = "http://172.17.13.31/healthCloud-open-gateway/gateway/internalGetDoctorInfo";
    //生产总线
    private static final String WANDA_url = "http://172.17.13.31/healthCloud-esb/esb/439B4414-48AB-4DBD-AD88-F4399063D945";
    @Autowired
    private DoctorMapper doctorMapper;

    @Autowired
    private DoctorAreaMapper doctorAreaMapper;

    @Autowired
    private DoctorInfoLogMapper doctorInfoLogMapper;

    @Autowired
    private DoctorVisitLogMapper doctorVisitLogMapper;

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public Integer save(Doctor doctor) {
        Date now = new Date();
        doctor.setCreateTime(now);
        doctor.setUpdateTime(now);
        doctorMapper.save(doctor);
        return doctor.getId();
    }

    @Override
    public void update(Doctor doctor) {
        doctor.setCreateTime(new Date());
        doctor.setUpdateTime(new Date());
        doctorMapper.save(doctor);
    }

    @Override
    public void delete(Doctor courseEvaluate) {
        doctorMapper.delete(courseEvaluate);
    }

    public Page<Doctor> pageList(Doctor doctor){
        Sort sort = new Sort(Sort.Direction.DESC, "updateTime");
        Pageable pageable=new PageRequest(doctor.page(), doctor.getLimit(),sort);  //分页信息
        Specification<Doctor> spec = new Specification<Doctor>() {        //查询条件构造

            @Override
            public Predicate toPredicate(Root<Doctor> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<Predicate>();
                if(StringUtils.isNotBlank(doctor.getName())){
                    Path<String> name = root.get("name");
                    predicates.add(cb.like(name, "%"+doctor.getName()+"%"));
                }
                if(StringUtils.isNotBlank(doctor.getPhone())){
                    Path<String> phone = root.get("phone");
                    predicates.add(cb.like(phone, "%"+doctor.getPhone()+"%"));
                }
                if(StringUtils.isNotBlank(doctor.getCompany())){
                    Path<String> company = root.get("company");
                    predicates.add(cb.like(company, "%"+doctor.getCompany()+"%"));
                }
                return cb.and(predicates
                        .toArray(new Predicate[] {}));
            }
        };
        Page<Doctor> plist =  doctorMapper.findAll(spec,pageable);
        List<Doctor> list = plist.getContent();
        for(Doctor doctor1 : list){
            if(null!=doctor1.getArea()&&!"".equals(doctor1.getArea())){
                //区域
                DoctorArea doctorArea = doctorAreaMapper.findByareaCode(doctor1.getArea());
                doctor1.setAreaName(doctorArea.getAreaName());
            }
        }
        return plist;
    }

    @Override
    public Doctor queryByUid(String uid){
        return doctorMapper.queryByUid(uid);
    }

    @Override
    public Doctor queryById(Integer id){
        return doctorMapper.queryById(id);
    }

    @Override
    public Doctor getDoctorInfo(String uid,String token,Integer doctorId){
        Map<String, String> params = new HashMap<>();
        params.put("uid",uid);
        try {
            //JSONObject jsonObject = HttpClientUtil.httpsGetTest(WANDA_url,params,token,WANDA_channelCode,WANDA_lightAppCode);
            JSONObject jsonObject = HttpClientUtil.httpsGet(WANDA_url,params,WANDA_authKey);
            Date now = new Date();
            String out = jsonObject.toJSONString();
            DoctorInfoLog doctorInfoLog = new DoctorInfoLog();
            doctorInfoLog.setUrl(WANDA_url);
            doctorInfoLog.setUid(uid);
            doctorInfoLog.setToken(token);
            doctorInfoLog.setOuts(out);
            doctorInfoLog.setCreateTime(now);
            doctorInfoLog.setDoctorId(doctorId);
            doctorInfoLogMapper.save(doctorInfoLog);
            Integer code = jsonObject.getInteger("code");
            if(code == 0){
                Doctor doctor = new Doctor();
                JSONObject info = jsonObject.getJSONObject("data");
                doctor.setName(info.getString("name"));
                doctor.setPhone(info.getString("mobile"));
                log.info("doctype:"+info.getInteger("doctype"));
                if(null==info.getInteger("doctype")){
                    doctor.setProperty(0);//乡村医生
                    log.info("================================乡村医生doctype空");
                }else if(info.getInteger("doctype")==2){
                    doctor.setProperty(0);//乡村医生
                    log.info("================================乡村医生");
                }else{
                    doctor.setProperty(1);//全科医生
                    log.info("================================全科医生");
                }
                doctor.setCompany(info.getString("hospital_name"));
                doctor.setTitle(info.getString("duty_name"));
                doctor.setUid(info.getString("uid"));
                doctor.setPhoto(info.getString("avatar"));
                doctor.setArea(info.getString("area"));
                doctor.setGender(info.getString("gender"));
                //12-3，是否实名统一为是info.getInteger("auditing_status")
                doctor.setAuditingStatus(1);
                //doctorId不为空，更新医生信息
                if(null != doctorId && !"".equals(doctorId)){
                    doctorMapper.update(doctor.getProperty(),info.getString("area"),info.getString("avatar"),info.getString("hospital_name"),doctorId);
                    //doctorMapper.update(doctor.getProperty(),info.getString("name"),info.getString("mobile"),info.getString("gender"),info.getString("duty_name"),info.getString("area"),info.getString("avatar"),info.getString("hospital_name"),doctorId);
                }
                //保存访问日志
                DoctorVisitLog doctorVisitLog = new DoctorVisitLog();
                doctorVisitLog.setUid(uid);
                doctorVisitLog.setName(info.getString("name"));
                doctorVisitLog.setCreateTime(now);
                doctorVisitLogMapper.save(doctorVisitLog);
                return doctor;
            }
            if(code == 11){//token失效
                throw new BuzEx(CCode.C_Token_OutTime,jsonObject.getString("msg"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Doctor getOne(Integer id) {
        return doctorMapper.getOne(id);
    }
}
