package com.adinnet.service.Impl;

import com.adinnet.dao.SysRoleMapper;
import com.adinnet.repository.SysRole;
import com.adinnet.service.SysUserRolesService;
import com.adinnet.dao.SysUserRolesMapper;
import com.adinnet.repository.SysUserRole;
import com.adinnet.utils.DateUtils;
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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author wangren
 * @Description: 用户角色
 * @create 2018-09-13 17:11
 **/
@Service
public class SysUserRolesServiceImpl implements SysUserRolesService {

    @Autowired
    private SysUserRolesMapper sysUserRolesMapper ;

    @Autowired
    private SysRoleMapper sysRoleMapper ;

    @Override
    public List<SysUserRole> queryByUserId(Integer adminId) {
        return sysUserRolesMapper.queryByUserId(adminId);
    }

    @Override
    public List<SysRole> selectRoleListByAdminId(Integer adminId) {

        return sysUserRolesMapper.findByAdminId(adminId);
    }

    @Override
    public List<SysRole> selectAll() {
        return sysRoleMapper.findAll();
    }
    @Override
    public Page<SysRole> pageList( SysRole sysRole){
        Sort sort = new Sort(Sort.Direction.DESC, "updatedAt");
        Pageable pageable=new PageRequest(sysRole.page(), sysRole.getLimit(),sort);  //分页信息
        Specification<SysRole> spec = new Specification<SysRole>() {        //查询条件构造
          @Override
            public Predicate toPredicate(Root<SysRole> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
              List<Predicate> predicates = new ArrayList<Predicate>();
              if(null != sysRole.getRoleName()){
                 Path<String> name = root.get("roleName");
                  predicates.add(cb.like(name, "%"+sysRole.getRoleName()+"%"));
             }
             if(null != sysRole.getEnable()){
                 Path<Integer> age = root.get("enable");
                 predicates.add(cb.equal(age, sysRole.getEnable()));
             }
              return cb.and(predicates
                      .toArray(new Predicate[] {}));
             }
         };
        return sysRoleMapper.findAll(spec,pageable);
    }

    @Override
    public SysRole getOne(Integer id) {
        return sysRoleMapper.getOne(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void save(SysUserRole sysUserRole) {
        sysUserRole.setIsDelete(1);
        sysUserRolesMapper.save(sysUserRole);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void saveRole(SysRole sysRole) {
        sysRole.setUpdatedAt(DateUtils.getNowDateString());
        sysRole.setCreatedAt(DateUtils.getNowDateString());
        sysRoleMapper.save(sysRole);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void updateRole(SysRole sysRole) {
        sysRole.setUpdatedAt(DateUtils.getNowDateString());
        sysRoleMapper.saveAndFlush(sysRole);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void saveOrUpDateRole(SysRole sysRole) {
        if (null == sysRole.getId()) {
            saveRole(sysRole);
        } else {
            updateRole(sysRole);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void delAllByUid(Integer adminId) {
        sysUserRolesMapper.delAllBeUid(adminId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void delRoles(Integer[] ids) {
        for(Integer id : ids ) {
            sysRoleMapper.deleteById(id);
            sysUserRolesMapper.delAllByRoleId(id);
        }
    }
}
