package adinnet.service.Impl;

import com.adinnet.dao.SysUserMapper;
import com.adinnet.dao.SysUserRoleSaveMapper;
import com.adinnet.repository.SysRole;
import com.adinnet.repository.SysUserRole;
import com.adinnet.repository.SysUserRoleSave;
import com.adinnet.repository.UserInfo;
import com.adinnet.service.SysUserRolesService;
import com.adinnet.service.SysUserService;
import com.adinnet.utils.DateUtils;
import com.adinnet.utils.PasswordUtil;
import com.adinnet.utils.ReturnUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.validation.ObjectError;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wangren
 * @Description: 用户
 * @create 2018-09-13 17:30
 **/
@Service
@Slf4j
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SysUserRolesService sysUserRolesService;
    @Autowired
    private SysUserRoleSaveMapper sysUserRoleSaveMapper;
    @Override
    public UserInfo queryByUserName(String username) {

        return sysUserMapper.queryByUserName(username);
    }


    public Page<UserInfo> pageList( UserInfo userInfo){
        Sort sort = new Sort(Sort.Direction.DESC, "updatedAt");
        Pageable pageable=new PageRequest(userInfo.page(), userInfo.getLimit(),sort);  //分页信息
        Specification<UserInfo> spec = new Specification<UserInfo>() {        //查询条件构造
            @Override
            public Predicate toPredicate(Root<UserInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<Predicate>();
                if(null != userInfo.getUsername()){
                    Path<String> name = root.get("username");
                    predicates.add(cb.like(name, "%"+userInfo.getUsername()+"%"));
                }
                return cb.and(predicates
                        .toArray(new Predicate[] {}));
            }
        };
        return sysUserMapper.findAll(spec,pageable);
    }

    @Override
    public UserInfo queryById(Integer uid) {
        return sysUserMapper.getOne(uid);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void save(UserInfo userInfo) {
        userInfo.setCreatedAt(DateUtils.getNowDateString());
        userInfo.setUpdatedAt(DateUtils.getNowDateString());
        userInfo.setState(1);
        String salt = new SecureRandomNumberGenerator().nextBytes().toHex();
        userInfo.setSalt(salt);
        String password = PasswordUtil.createAdminPwd(userInfo.getPassword(), userInfo.getUsername()+salt);
        userInfo.setPassword(password);
        userInfo = sysUserMapper.save(userInfo);
        for(Integer role:userInfo.getRoleId()){
            SysUserRoleSave sysUserRole  = new SysUserRoleSave();
            sysUserRole.setAdminId(userInfo.getUid());
            sysUserRole.setRoleId(role);
            sysUserRole.setIsDelete(1);
            sysUserRoleSaveMapper.save(sysUserRole);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public ModelMap saveOrUpdate(UserInfo userInfo) {
        UserInfo user = queryByUserName(userInfo.getUsername());
        if (null == userInfo.getUid() ) {
            if (null != user) {
                return ReturnUtil.Error("用户名已存在", null, null);
            }
            if (null == userInfo.getPassword()) {
                return ReturnUtil.Error("密码不能为空", null, null);
            }
            save(userInfo);
        }else {
            if(null != user && user.getUid().equals(userInfo.getUid()) && !user.getUsername().equals(userInfo.getUsername())){
                return ReturnUtil.Error("用户名已存在", null, null);
            }
            update(userInfo);
        }
        return ReturnUtil.Success("操作成功", null, "/api/admin/index");
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void update(UserInfo userInfo) {
        UserInfo user = queryById(userInfo.getUid());
        userInfo.setUpdatedAt(DateUtils.getNowDateString());
       if(null != userInfo.getPassword()){
           String password = PasswordUtil.createAdminPwd(userInfo.getPassword(), user.getCredentialsSalt());
           userInfo.setPassword(password);
       }else{
           userInfo.setPassword(user.getPassword());
       }
        userInfo.setSalt(user.getSalt());
        sysUserMapper.saveAndFlush(userInfo);
        sysUserRolesService.delAllByUid(userInfo.getUid());
        if( null != userInfo.getRoleId()){
            for(Integer role:userInfo.getRoleId()){
                SysUserRoleSave sysUserRole  = new SysUserRoleSave();
                sysUserRole.setAdminId(userInfo.getUid());
                sysUserRole.setIsDelete(1);
                sysUserRole.setRoleId(role);
                sysUserRoleSaveMapper.save(sysUserRole);
            }
        }
    }

    @Override
    @Transactional
    public void del(Integer uid) {
        sysUserMapper.deleteById(uid);
    }
    @Override
    public void updatepwd(UserInfo userInfo) {
        sysUserMapper.saveAndFlush(userInfo);
    }
}
