package adinnet.service.Impl;

import com.adinnet.dao.SpecialistMapper;
import com.adinnet.http.HttpUtils;
import com.adinnet.repository.CourseSpecialist;
import com.adinnet.repository.Specialist;
import com.adinnet.service.SpecialistService;
import com.adinnet.utils.qiniu.QiniuUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.criteria.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by adinnet on 2018/9/20.
 */
@Service
public class SpecialistServiceImpl implements SpecialistService {

    @Autowired
    private SpecialistMapper specialistMapper;

    @Override
    public List<Specialist> querySpecialistByCourse(Integer courseId){
        return specialistMapper.querySpecialistByCourse(courseId);
    }

    @Override
    public String querySpecialistName(Integer courseId) {
        StringBuffer bu = new StringBuffer();
        List<Specialist> list  = specialistMapper.querySpecialistByCourse(courseId);
        for(Specialist specialist : list){
            bu.append(specialist.getName()+"  ");
        }
        return bu.toString();
    }

    public Page<Specialist> pageList(Specialist specialist){
        Sort sort = new Sort(Sort.Direction.DESC, "updateTime");
        Pageable pageable=new PageRequest(specialist.page(), specialist.getLimit(),sort);  //分页信息
        Specification<Specialist> spec = new Specification<Specialist>() {        //查询条件构造

            @Override
            public Predicate toPredicate(Root<Specialist> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<Predicate>();
                predicates.add(cb.equal(root.get("isDelete"), 1));
                if(StringUtils.isNotBlank(specialist.getName())){
                    Path<String> name = root.get("name");
                    predicates.add(cb.like(name, "%"+specialist.getName()+"%"));
                }
                return cb.and(predicates
                        .toArray(new Predicate[] {}));
            }
        };
        return specialistMapper.findAll(spec,pageable);
    }

    @Override
    public Specialist queryById(Integer id) {
        return specialistMapper.queryById(id);
    }

    @Override
    public void saveSpecialist(Specialist specialist, MultipartFile file) throws IOException, HttpException {
        if(file != null){
            String path = QiniuUtils.upload(file, "specialistImage");
            specialist.setPhoto(path);
        }
        specialist.setIsDelete(1);
        specialist.setCreateTime(new Date());
        specialist.setUpdateTime(new Date());
        specialistMapper.save(specialist);
    }

    @Override
    public void updateSpecialist(Specialist specialist, MultipartFile file) throws IOException, HttpException {
        Specialist specialist1 = specialistMapper.queryById(specialist.getId());
        if(file.getOriginalFilename() !=null &&!"".equals(file.getOriginalFilename())){
            String path = QiniuUtils.upload(file, "specialistImage");
            specialist.setPhoto(path);
        }else{
            specialist.setPhoto(specialist1.getPhoto());
        }
        specialist.setIsDelete(1);
        specialist.setCreateTime(specialist1.getCreateTime());
        specialist.setUpdateTime(new Date());
        specialistMapper.saveAndFlush(specialist);
    }

    @Override
    @Transactional
    public void del(Integer id) {
        specialistMapper.updateById(id);
    }

    public List<Specialist> getList(Integer id){
        List<Specialist> list = specialistMapper.querySpecialistAll();
        List<Specialist> clist = specialistMapper.querySpecialistByCourse(id);
        for(Specialist specialist:clist){
            for(Specialist specialist1:list){
                if(specialist1.getId()==specialist.getId()){
                    specialist1.setChecked(1);
                }else{
                    if(null==specialist1.getChecked()){
                        specialist1.setChecked(0);
                    }
                }
            }
        }
        return list;
    }
}
