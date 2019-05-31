package adinnet.service.Impl;

import com.adinnet.dao.SysMenuMapper;
import com.adinnet.dao.SysRolesMenuMapper;
import com.adinnet.repository.Menu;
import com.adinnet.repository.SysRoleMenu;
import com.adinnet.service.SysRolesMenuService;
import com.adinnet.utils.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author wangren
 * @Description: 角色权限
 * @create 2018-09-13 17:16
 **/
@Service
public class SysRolesMenuServiceImpl implements SysRolesMenuService {

    @Autowired
    private SysMenuMapper sysMenuMapper;

    @Autowired
    private SysRolesMenuMapper sysRolesMenuMapper;

    @Override
    public Iterable<Menu> queryPermissionsByRoles(Set<Integer> ids) {
        return  sysMenuMapper.findAllById(ids);
    }

    @Override
    public List<Menu> queryAllPermissions() {
        return sysMenuMapper.queryAll();
    }

    @Override
    public List<Menu> selectMenuByAdminId(Integer uid) {
        return sysMenuMapper.queryByUid(uid);
    }

    @Override
    public List<Menu> queryByParentId(Integer parentid) {
        return sysMenuMapper.queryByParentId(parentid);
    }

    @Override
    public List<Menu> getChildMenuList(ArrayList<Menu> menuLists, Integer parentId) {
        List<Menu> list = sysMenuMapper.queryByParentId(parentId);
        for(Menu menu:list){
            menuLists.add(menu);
            getChildMenuList(menuLists,menu.getMenuId());
        }
       return menuLists;
    }

    @Override
    public Menu selectById(Integer id) {
        return sysMenuMapper.getOne(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void save(Menu menu) {
        menu.setCreatedAt(DateUtils.getNowDateString());
        menu.setUpdatedAt(DateUtils.getNowDateString());
        menu.setIsDelete(1);
        sysMenuMapper.save(menu);
    }

    @Override
    public void saveRoleMenu(Integer roleId, Integer[] menuIds) {
        for(Integer id : menuIds){
            SysRoleMenu sysRoleMenu = new SysRoleMenu();
            sysRoleMenu.setMenuId(id);
            sysRoleMenu.setRoleId(roleId);
            sysRolesMenuMapper.save(sysRoleMenu);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void saveRoleMenuFlag(Integer roleId, Integer[] menuIds) {
        if (menuIds != null && null != roleId) {
            if (StringUtils.isNotEmpty(menuIds.toString())) {
                delRoleMenu(roleId);
                saveRoleMenu(roleId,menuIds);
            }
        } else if (menuIds == null && null != roleId) {
            delRoleMenu(roleId);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void delRoleMenu(Integer roleId) {
        sysRolesMenuMapper.deleteSysRoleMenu(roleId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void update(Menu menu) {
        menu.setUpdatedAt(DateUtils.getNowDateString());
        menu.setIsDelete(1);
        sysMenuMapper.saveAndFlush(menu);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void saveOrUpdate(Menu menu) {
        if(null == menu.getMenuId()){
           save(menu);
        }else {
            update(menu);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void delMenu(Integer[] ids) {
        for (Integer id : ids) {
            sysMenuMapper.upDateFlag(id);
        }
    }

    @Override
    public List<SysRoleMenu> selectByRoleId(Integer roleId) {
        Specification<SysRoleMenu> spec = new Specification<SysRoleMenu>() {        //查询条件构造
            @Override
            public Predicate toPredicate(Root<SysRoleMenu> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<Predicate>();
                    Path<Integer> id = root.get("roleId");
                    predicates.add(cb.equal(id, roleId));
                return cb.and(predicates
                        .toArray(new Predicate[] {}));
            }
        };
        return sysRolesMenuMapper.findAll(spec);
    }
}
