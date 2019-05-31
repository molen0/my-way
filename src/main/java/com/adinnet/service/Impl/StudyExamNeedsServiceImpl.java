package com.adinnet.service.Impl;

import com.adinnet.common.status.IsDeleted;
import com.adinnet.common.status.IsJudge;
import com.adinnet.dao.StudyExamNeedsMapper;
import com.adinnet.ex.BuzEx;
import com.adinnet.repository.Menu;
import com.adinnet.repository.StudyExamNeeds;
import com.adinnet.response.code.BuzCode;
import com.adinnet.response.code.CCode;
import com.adinnet.service.StudyExamNeedsService;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by wangx on 2018/9/18.
 */
@Service
@Slf4j
public class StudyExamNeedsServiceImpl implements StudyExamNeedsService {
    @Autowired
    private StudyExamNeedsMapper studyExamNeedsMapper;


    @Override
    public JSONObject queryAllNeeds(Integer type,Integer  status) {
        List<StudyExamNeeds> studyExamNeeds = null;
        JSONObject jo = new JSONObject();
        if (null != status && type ==1) {
            studyExamNeeds = studyExamNeedsMapper.findAllList(type, status);

        }else if(type ==0) {
        studyExamNeeds = studyExamNeedsMapper.findList(type);
        }
          if (studyExamNeeds ==null){
              studyExamNeeds = new ArrayList<>();
          }
            jo.put("studyExamNeeds", studyExamNeeds);

        return jo;
    }
    @Override
    public Page<StudyExamNeeds> pageList(StudyExamNeeds studyExamNeeds) {
        Sort sort = new Sort(Sort.Direction.DESC, "updateTime");
        Pageable pageable = new PageRequest(studyExamNeeds.page(), studyExamNeeds.getLimit(), sort);  //分页信息
        Specification<StudyExamNeeds> spec = new Specification<StudyExamNeeds>() {        //查询条件构造
            @Override
            public Predicate toPredicate(Root<StudyExamNeeds> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<Predicate>();
                if (null != studyExamNeeds.getPhotoUrl()) {
                    Path<String> name = root.get("photoUrl");
                    predicates.add(cb.like(name, "%" + studyExamNeeds.getPhotoUrl() + "%"));
                }

                Path<Integer> age = root.get("isDeleted");
                predicates.add(cb.equal(age, 1));

                return cb.and(predicates
                        .toArray(new Predicate[]{}));
            }
        };
        return studyExamNeedsMapper.findAll(spec, pageable);
    }

    @Override
    public Integer save(StudyExamNeeds studyExamNeeds) {
        StudyExamNeeds se = null;

        try {
            if (null != studyExamNeeds) {
                se = studyExamNeedsMapper.save(studyExamNeeds);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return se.getId();
    }

    @Override
    @Transactional
    public Integer update(StudyExamNeeds studyExamNeeds) {
        if (null != studyExamNeeds) {
            Integer id = studyExamNeeds.getId();
            String title = studyExamNeeds.getTitle();
            // String photoUrl = studyExamNeeds.getPhotoUrl();
            String textArea = studyExamNeeds.getTextArea();
           int status =   studyExamNeeds.getStatus();
            int type = studyExamNeeds.getType();
            int isDeleted =IsDeleted._1.getCode();
            Integer result = studyExamNeedsMapper.updateById(id, title, type, textArea, isDeleted,status);
            return result;
        }

        return null;
    }

    @Override
    @Transactional
    public Integer deleteById(Integer id) {
        if (null == id) {
           log.error("needsId is null");
            throw new BuzEx (CCode.C_PARAM_IS_NULL);
        }
        return studyExamNeedsMapper.delById(id);


    }

    @Override
    public StudyExamNeeds getOne(Integer id) {

        if (null == id) {
            log.error("needsId is null");
            throw new BuzEx (CCode.C_PARAM_IS_NULL);
        }
        return studyExamNeedsMapper.getOne(id);
    }
}
