package adinnet.service.Impl;

import com.adinnet.dao.CourseMapper;
import com.adinnet.dao.SemesterMapper;
import com.adinnet.repository.Course;
import com.adinnet.repository.Examin;
import com.adinnet.repository.Semester;
import com.adinnet.service.SemesterService;
import com.adinnet.utils.qiniu.QiniuUtils;
import org.apache.http.HttpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.criteria.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by RuanXiang on 2018/9/20.
 */
@Service
public class SemesterServiceImpl implements SemesterService {
    @Autowired
    private SemesterMapper semesterMapper;
    @Autowired
    private CourseMapper courseMapper;
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void save(Semester semester) {
        semester.setCreateTime(new Date());
        semester.setUpdateTime(new Date());
        semesterMapper.save(semester);
    }

    @Override
    public void update(Semester semester) {
        semester.setCreateTime(new Date());
        semester.setUpdateTime(new Date());
        semesterMapper.save(semester);
    }

    @Override
    public void delete(Semester semester) {
        semesterMapper.delete(semester);
    }

    public Page<Semester> pageList(Semester semester){
        Sort sort = new Sort(Sort.Direction.DESC, "updateTime");
        Pageable pageable=new PageRequest(semester.page(), semester.getLimit(),sort);  //分页信息
        Specification<Semester> spec = new Specification<Semester>() {        //查询条件构造

            @Override
            public Predicate toPredicate(Root<Semester> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<Predicate>();
                Path<Integer> isDelete = root.get("isDelete");
                predicates.add(cb.equal(isDelete, 1));
                return cb.and(predicates
                        .toArray(new Predicate[] {}));
            }
        };
        Page<Semester> plist = semesterMapper.findAll(spec,pageable);
        List<Semester> list = plist.getContent();
        for(Semester s:list){
            //视频大纲数
            List<Course> courses = courseMapper.queryBySemesterId(s.getId());
            s.setCourseCount(courses==null?0:courses.size());
        }
        return plist;
    }

    @Override
    public List<Semester> selectAllList() {
        Semester semester = new Semester();
        semester.setIsDelete(1);
        //创建匹配器，即如何使用查询条件
        ExampleMatcher matcher = ExampleMatcher.matching() //构建对象
                .withMatcher("isDelete", ExampleMatcher.GenericPropertyMatchers.exact());
        //创建实例
        Example<Semester> ex = Example.of(semester);
       // List<Semester> list =  semesterMapper.findAll(ex);
        return semesterMapper.findAll(ex);
    }

    @Override
    public Semester queryThisSemester(){
        return semesterMapper.queryThisSemester();
    }

    @Override
    public Semester queryByCourseId(Integer courseId) {
        Course course = courseMapper.getOne(courseId);
        return semesterMapper.queryById(course.getSemesterId());
    }

    @Override
    public Semester queryById(Integer id) {
        return semesterMapper.queryById(id);
    }

    @Override
    @Transactional
    public void saveSemester(Semester semester){
        if(semester.getStatus()==1){
            semesterMapper.updateOthers();
        }
        semester.setUserId(1);
        semester.setCreateTime(new Date());
        semester.setUpdateTime(new Date());
        semester.setIsDelete(1);
        semesterMapper.save(semester);
    }

    @Override
    @Transactional
    public void updateSemester(Semester semester){
        if(semester.getStatus()==1){
            semesterMapper.updateOthers();
        }
        Semester semester1 = semesterMapper.queryById(semester.getId());
        semester.setUserId(semester1.getUserId());
        semester.setCreateTime(semester1.getCreateTime());
        semester.setUpdateTime(new Date());
        semester.setIsDelete(1);
        semesterMapper.saveAndFlush(semester);
    }

    @Override
    public Semester isHas(){
        Semester semester = semesterMapper.queryThisSemester();
        return semester;
    }

    @Override
    @Transactional
    public void del(Integer id) {
        semesterMapper.updateById(id);
    }
    @Override
    public List<Semester> queryCreaditBookOthers(){
        return semesterMapper.queryCreaditBookOthers();
    }

    @Override
    public List<Semester> queryAllSemester() {
        return semesterMapper.queryAllSemester();
    }
}
