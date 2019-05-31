package adinnet.service.Impl;

import com.adinnet.dao.CreaditBookMapper;
import com.adinnet.repository.CreaditBook;
import com.adinnet.repository.Mail;
import com.adinnet.repository.Semester;
import com.adinnet.repository.SysRole;
import com.adinnet.service.CreaditBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author wangren
 * @Description: z证书模板
 * @create 2018-10-09 15:19
 **/
@Service
public class CreaditBookServiceImpl implements CreaditBookService {

    @Autowired
    private CreaditBookMapper  creaditBookMapper;
    @Override
    public Page<CreaditBook> pageList(CreaditBook creaditBook) {
        Sort sort = new Sort(Sort.Direction.DESC, "updateTime");
        Pageable pageable=new PageRequest(creaditBook.page(), creaditBook.getLimit(),sort);  //分页信息
        Specification<CreaditBook> spec = new Specification<CreaditBook>() {        //查询条件构造
            @Override
            public Predicate toPredicate(Root<CreaditBook> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<Predicate>();
                    Path<Integer> isDelete = root.get("isDelete");
                    predicates.add(cb.equal(isDelete, 1));
                return cb.and(predicates
                        .toArray(new Predicate[] {}));
            }
        };
        return creaditBookMapper.findAll(spec,pageable);
    }

    @Override
    public void saveOrUpdate(CreaditBook creaditBook) {
     if(null != creaditBook.getId()){
//         Semester semester = new Semester();
//         semester.setId(creaditBook.getSemesterId());
//         creaditBook.setSemester(semester);
         creaditBook.setIsDelete(1);
         creaditBook.setUpdateTime(new Date());
         creaditBookMapper.saveAndFlush(creaditBook);
     }else{
//         Semester semester = new Semester();
//         semester.setId(creaditBook.getSemesterId());
//         creaditBook.setSemester(semester);
         creaditBook.setIsDelete(1);
         creaditBook.setUpdateTime(new Date());
         creaditBook.setCreateTime(new Date());
         creaditBook = creaditBookMapper.saveAndFlush(creaditBook);
     }
    }

    @Override
    public CreaditBook getOne(Integer id) {
        return creaditBookMapper.getOne(id);
    }

    @Override
    public void del(Integer id) {
        String hql = "update tb_cradit_model set is_delete = 0 ,update_time = now() where id = ?";
        creaditBookMapper.updateBean(hql,id);
    }

    @Override
    public CreaditBook getBysemesterId(Integer semesterId) {
        CreaditBook creaditBook = new CreaditBook();
        creaditBook.setIsDelete(1);
        creaditBook.setSemesterId(semesterId);
//        Semester semester = new Semester();
//        semester.setId(semesterId);
//        creaditBook.setSemester(semester);
        //创建匹配器，即如何使用查询条件
        ExampleMatcher matcher = ExampleMatcher.matching() //构建对象
                .withMatcher("isDelete", ExampleMatcher.GenericPropertyMatchers.exact()) ;
        //创建实例
        Example<CreaditBook> ex = Example.of(creaditBook, matcher);
        List<CreaditBook> list =creaditBookMapper.findAll(ex);
        if(list.size()>0){
            creaditBook = list.get(0);
        }
        return creaditBook;
    }

}
