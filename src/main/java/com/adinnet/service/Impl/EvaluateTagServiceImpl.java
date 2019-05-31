package com.adinnet.service.Impl;

import com.adinnet.common.status.IsJudge;
import com.adinnet.dao.CourseEvaluateMapper;
import com.adinnet.dao.EvaluateTagMapper;
import com.adinnet.repository.EvaluateTag;
import com.adinnet.service.EvaluateTagService;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
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
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by hasee on 2018/9/18.
 */
@Service
@Slf4j
public class EvaluateTagServiceImpl implements EvaluateTagService {
    @Autowired
    private EvaluateTagMapper evaluateTagMapper;

    @Autowired
    private CourseEvaluateMapper courseEvaluateMapper;

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void save(EvaluateTag evaluateTag) {
        evaluateTag.setCreateTime(new Date());
        evaluateTag.setUpdateTime(new Date());
        evaluateTagMapper.save(evaluateTag);
    }

    @Override
    public void update(EvaluateTag evaluateTag) {
        evaluateTag.setCreateTime(new Date());
        evaluateTag.setUpdateTime(new Date());
        evaluateTagMapper.save(evaluateTag);
    }

    @Override
    public EvaluateTag getOne(Integer id) {
        return evaluateTagMapper.getOne(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void saveEvaluateTag(EvaluateTag evaluateTag) {
        evaluateTag.setCreateTime(new Date());
        evaluateTag.setUpdateTime(new Date());
        evaluateTagMapper.save(evaluateTag);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void updateEvaluateTag(EvaluateTag evaluateTag) {
        evaluateTag.setUpdateTime(new Date());
        evaluateTag.setCreateTime(evaluateTag.getCreateTime());
        evaluateTagMapper.saveAndFlush(evaluateTag);
    }

    @Override
    public void delete(EvaluateTag evaluateTag) {
        evaluateTagMapper.delete(evaluateTag);
    }

    @Override
    @Transactional
    public void del(Integer id) {
        evaluateTagMapper.deleteById(id);
    }

    public Page<EvaluateTag> pageList( EvaluateTag evaluateTag){
        Sort sort = new Sort(Sort.Direction.DESC, "updateTime");
        Pageable pageable=new PageRequest(evaluateTag.page(), evaluateTag.getLimit(),sort);  //分页信息
        Specification<EvaluateTag> spec = new Specification<EvaluateTag>() {        //查询条件构造
            @Override
            public Predicate toPredicate(Root<EvaluateTag> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<Predicate>();
                if(null != evaluateTag.getContent()){
                    Path<String> name = root.get("content");
                    predicates.add(cb.like(name, "%"+evaluateTag.getContent().trim()+"%"));
                }
                return cb.and(predicates
                        .toArray(new Predicate[] {}));
            }
        };
        return evaluateTagMapper.findAll(spec,pageable);
    }

    @Override
    public JSONObject queryAllTagList(){
        Integer type_0= IsJudge.NO.getCode();//视频印象
        Integer type_1= IsJudge.YES.getCode();//期待上的课程
        List<EvaluateTag> impression = evaluateTagMapper.queryTagByType(type_0);
        List<EvaluateTag> course = evaluateTagMapper.queryTagByType(type_1);
        JSONObject jo=new JSONObject();

        jo.put("impression",impression);

        jo.put("course",course);
        log.info("queryAllTagList  :"+jo.toString());
        return jo;
    }

    @Override
    public List<EvaluateTag> getReportList(EvaluateTag evaluateTag) {
        if(evaluateTag.getType()==null){
            evaluateTag.setType(IsJudge.NO.getCode());
        }
        List<EvaluateTag> list = evaluateTagMapper.queryTagByType(evaluateTag.getType());
        for(EvaluateTag evaluateTag1:list){
            if(evaluateTag.getType() == IsJudge.NO.getCode()){//视频印象
                BigInteger nums = courseEvaluateMapper.findNumsByTagId1(evaluateTag1.getId());
                Integer num = Integer.parseInt(nums.toString());
               evaluateTag1.setNums(num);
            }else if(evaluateTag.getType() == IsJudge.YES.getCode()){//期待课程
                BigInteger nums = courseEvaluateMapper.findNumsByTagId2(evaluateTag1.getId());
                Integer num = Integer.parseInt(nums.toString());
                evaluateTag1.setNums(num);
            }
        }
        return list;
    }

    @Override
    public JSONObject queryAllTagList2() {
        Integer type_0= IsJudge.NO.getCode();//视频印象
        Integer type_1= IsJudge.YES.getCode();//期待上的课程
        List<EvaluateTag> impression = evaluateTagMapper.queryTagByType(type_0);
        for(EvaluateTag evaluateTag1:impression){
            BigInteger nums = courseEvaluateMapper.findNumsByTagId1(evaluateTag1.getId());
            Integer num = Integer.parseInt(nums.toString());
            evaluateTag1.setNums(num);
        }
        List<EvaluateTag> course = evaluateTagMapper.queryTagByType(type_1);
        for(EvaluateTag evaluateTag1:course){
            BigInteger nums = courseEvaluateMapper.findNumsByTagId2(evaluateTag1.getId());
            Integer num = Integer.parseInt(nums.toString());
            evaluateTag1.setNums(num);
        }
        JSONObject jo=new JSONObject();
        jo.put("impression",impression);
        jo.put("course",course);
        log.info("queryAllTagList  :"+jo.toString());
        return jo;
    }
}
