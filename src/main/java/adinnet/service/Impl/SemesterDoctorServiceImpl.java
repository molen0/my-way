package adinnet.service.Impl;

import com.adinnet.dao.SemesterDoctorMapper;
import com.adinnet.repository.EmailBook;
import com.adinnet.repository.Semester;
import com.adinnet.repository.SemesterDoctor;
import com.adinnet.service.DoctorService;
import com.adinnet.service.SemesterDoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by adinnet on 2018/9/20.
 */
@Service
public class SemesterDoctorServiceImpl implements SemesterDoctorService {
    @Autowired
    private SemesterDoctorMapper semesterDoctorMapper;

    @Autowired
    private DoctorService doctorService;
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void save(SemesterDoctor semesterDoctor) {
        semesterDoctor.setCreateTime(new Date());
        semesterDoctor.setUpdateTime(new Date());
        semesterDoctorMapper.save(semesterDoctor);
    }

    @Override
    public void update(SemesterDoctor semesterDoctor) {
        semesterDoctor.setCreateTime(new Date());
        semesterDoctor.setUpdateTime(new Date());
        semesterDoctorMapper.save(semesterDoctor);
    }

    @Override
    public void delete(SemesterDoctor semesterDoctor) {
        semesterDoctorMapper.delete(semesterDoctor);
    }

    @Override
    public SemesterDoctor queryIsSignUp(Integer semesterId,Integer doctorId){
        return semesterDoctorMapper.queryIsSignUp(semesterId,doctorId);
    }

    @Override
    public  List<SemesterDoctor> querysemesterId(Integer semesterId) {

        SemesterDoctor semesterDoctor = new SemesterDoctor();
        semesterDoctor.setSemesterId(semesterId);
        ExampleMatcher matcher = ExampleMatcher.matching() //构建对象
                .withMatcher("semesterId", ExampleMatcher.GenericPropertyMatchers.exact());
        //创建实例
        Example<SemesterDoctor> ex = Example.of(semesterDoctor);
        return semesterDoctorMapper.findAll(ex);
    }
}
