package adinnet.service.Impl;

import com.adinnet.dao.ExaminMapper;
import com.adinnet.dao.ExaminTopicAnswerMapper;
import com.adinnet.dao.ExaminTopicMapper;
import com.adinnet.repository.*;
import com.adinnet.service.ExaminTopicService;
import com.adinnet.service.SpecialistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author wangren
 * @Description: 考试课程
 * @create 2018-09-26 14:02
 **/
@Service
public class ExaminTopicServiceImpl implements ExaminTopicService {

    @Autowired
    private ExaminMapper examinMapper;
    @Autowired
    private ExaminTopicMapper examinTopicMapper;

    @Autowired
    private ExaminTopicAnswerMapper examinTopicAnswerMapper;
    @Autowired
    private SpecialistService specialistService;
    @Override
    public PageBean<Examin> pageList(Examin examin) {
        PageBean<Examin> page = new PageBean<>();
        String hql = "SELECT te.*,tc.* " +
                " FROM tb_examin te  \n" +
                "  LEFT JOIN tb_course tc ON te.course_id = tc.id where te.is_delete=1 ";
        String countHql = "SELECT count(1) " +
                " FROM tb_examin te  \n" +
                "  LEFT JOIN tb_course tc ON te.course_id = tc.id where te.is_delete=1 ";
        StringBuffer str = new StringBuffer(hql);
        StringBuffer str1 = new StringBuffer(countHql);
        if(null != examin){
            if(null != examin.getCtitle() && !"".equals(examin.getCtitle())){
                str.append("  and tc.title like concat('%','"+examin.getCtitle()+"','%') ");
                str1.append("  and  tc.title like concat('%','"+examin.getCtitle()+"','%') ");
            }
            if(null != examin.getCcourseAttr()){
                str.append("  and tc.course_attr = "+examin.getCcourseAttr());
                str1.append("  and tc.course_attr = "+examin.getCcourseAttr());
            }
            if(null != examin.getCcourseType()){
                str.append("  and tc.course_type = "+examin.getCcourseType());
                str1.append(" and  tc.course_type = "+examin.getCcourseType());
            }
        }
        page.setHql(str.toString());
        page.setCountHql(str1.toString());
        page.setSortName("te.update_time");
        page.setPageNum(examin.getOffset());
        page.setPageSize(examin.getLimit());
        PageBean<Examin> page1 = examinMapper.findNatPage(page);
        List<Examin> examinList = page1.getDatas();
        for(Examin examin1 : examinList){
           examin1.setSpecialListName(specialistService.querySpecialistName(examin1.getCourse().getId()));
        }
        return page1;
    }

    @Override
    public Integer getExaminId(Integer courseId) {
        Examin examin = new Examin();
        Course course = new Course();
        course.setId(courseId);
        examin.setCourse(course);
        examin.setIsDelete(1);
        //创建匹配器，即如何使用查询条件
        ExampleMatcher matcher = ExampleMatcher.matching() //构建对象
                .withMatcher("course.id", ExampleMatcher.GenericPropertyMatchers.exact())
                .withMatcher("isDelete", ExampleMatcher.GenericPropertyMatchers.exact());
        //创建实例
        Example<Examin> ex = Example.of(examin);
        List<Examin> list =  examinMapper.findAll(ex);
        Integer id = null;
        if(list.size()>0){
            id=list.get(0).getId();
        }
        return  id;
    }

    public PageBean<Examin> pageList1(Examin examin) {
        Sort sort = new Sort(Sort.Direction.DESC, "updatedAt");
        Pageable pageable=new PageRequest(examin.page(), examin.getLimit(),sort);  //分页信息
        Specification<Examin> spec = new Specification<Examin>() {        //查询条件构造
            @Override
            public Predicate toPredicate(Root<Examin> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<Predicate>();
                    Path<Integer> isDelete = root.get("isDelete");
                    predicates.add(cb.equal(isDelete, 1));
                return cb.and(predicates
                        .toArray(new Predicate[] {}));
            }
        };
        PageBean<Examin> pageBean = new PageBean<>();
        Page<Examin> page = examinMapper.findAll(spec,pageable);
        pageBean.setDatas(page.getContent());
        return pageBean;
    }
    @Override
    public List<ExaminTopic> listExaminTopic(Integer id) {
        ExaminTopic examinTopic = new ExaminTopic();
        examinTopic.setExaminId(id);
        examinTopic.setIsDelete(1);
        Sort sort = new Sort(Sort.Direction.ASC, "order");
        //创建匹配器，即如何使用查询条件
        ExampleMatcher matcher = ExampleMatcher.matching() //构建对象
                .withMatcher("examinId", ExampleMatcher.GenericPropertyMatchers.exact())
                .withMatcher("isDelete", ExampleMatcher.GenericPropertyMatchers.exact());
        //创建实例
        Example<ExaminTopic> ex = Example.of(examinTopic, matcher);
        return  examinTopicMapper.findAll(ex,sort);
    }

    @Override
    public List<ExaminTopicAnswer> listByAnswer(Integer id) {
        ExaminTopicAnswer examinTopicAnswer = new ExaminTopicAnswer();
        examinTopicAnswer.setIsDelete(1);
        examinTopicAnswer.setTopicId(id);
        Sort sort = new Sort(Sort.Direction.ASC, "order");
        ExampleMatcher matcher = ExampleMatcher.matching() //构建对象
                .withMatcher("topicId", ExampleMatcher.GenericPropertyMatchers.exact())
                .withMatcher("isDelete", ExampleMatcher.GenericPropertyMatchers.exact());
        //创建实例
        Example<ExaminTopicAnswer> ex = Example.of(examinTopicAnswer, matcher);
        return examinTopicAnswerMapper.findAll(ex,sort);
    }

    @Override
    @Transactional
    public void saveExamin(Integer[] courseIds) {
        for(Integer courseid : courseIds){
            Examin e = new Examin();
            e.setIsDelete(1);
            e.setCreateTime(new Date());
            e.setUpdateTime(new Date());
            Course course = new Course();
            course.setId(courseid);
            e.setCourse(course);
            examinMapper.save(e);
        }
    }

    @Override
    @Transactional
    public void saveExaminTopic(ExaminTopic examinTopic) {
        examinTopic.setIsDelete(1);
        examinTopic.setCreateTime(new Date());
        examinTopic.setUpdateTime(new Date());
        examinTopicMapper.save(examinTopic);
    }

    @Override
    @Transactional
    public void saveExaminTopicAnswer(ExaminTopicAnswer examinTopicAnswer) {
        examinTopicAnswer.setIsDelete(1);
        examinTopicAnswer.setCreateTime(new Date());
        examinTopicAnswer.setUpdateTime(new Date());
        examinTopicAnswerMapper.save(examinTopicAnswer);
    }

    @Override
    @Transactional
    public void updateExamin(Integer id, Integer courseId) {
        String hql = "UPDATE  tb_examin SET course_id = ?,update_time = now() WHERE id = ?";
        examinMapper.updateBean(hql,courseId,id);
    }

    @Override
    @Transactional
    public void updateExaminTopic(ExaminTopic examinTopic) {
        String hql = "UPDATE  tb_examin_topic SET topic_name = ?,topic_answer = ?,orders = ?,remark=?,type=?,update_time = now() WHERE id = ?";
        examinTopicMapper.updateBean(hql,examinTopic.getTopicName(),examinTopic.getTopicAnswer(),examinTopic.getOrder(),examinTopic.getRemark(),examinTopic.getType(),examinTopic.getId());
    }

    @Override
    @Transactional
    public void updateExaminTopicAnswer(ExaminTopicAnswer examinTopicAnswer) {
        String hql = "UPDATE  tb_examin_topic_answer SET answer_name = ?,orders = ?,update_time = now() WHERE id = ?";
        examinTopicAnswerMapper.updateBean(hql,examinTopicAnswer.getAnswerName(),examinTopicAnswer.getOrder(),examinTopicAnswer.getId());
    }

    @Override
    public ExaminTopic getOneExaminTopic(Integer id) {
        return examinTopicMapper.getOne(id);
    }

    @Override
    public ExaminTopicAnswer getOneExaminTopicAnswer(Integer id) {
        return examinTopicAnswerMapper.getOne(id);
    }

    @Override
    @Transactional
    public void delExaminTopic(Integer id) {
        String hql = "UPDATE  tb_examin_topic SET is_delete = 0,update_time = now() WHERE id = ?";
        examinTopicMapper.updateBean(hql,id);
    }

    @Override
    @Transactional
    public void delExaminTopicAnswer(Integer id) {
        String hql = "UPDATE  tb_examin_topic_answer SET is_delete = 0,update_time = now() WHERE id = ?";
        examinTopicAnswerMapper.updateBean(hql,id);
    }

    @Override
    @Transactional
    public void delExamin(Integer id) {
       // String hql = "UPDATE  tb_examin SET is_delete = 0,update_time = now() WHERE id = ?";
        examinMapper.deleteById(id);
    }
}
