package com.adinnet.service;

import com.adinnet.repository.Menu;
import com.adinnet.repository.SysRoleMenu;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author wangren
 * @Description: 角色资源
 * @create 2018-09-13 15:24
 **/
public interface SysRolesMenuService {
    /**
     * 根据多个角色查询
     * @param ids
     * @return
     */
    public Iterable<Menu> queryPermissionsByRoles(Set<Integer> ids);

    /**
     * 查询所有
     * @return
     */
    public List<Menu> queryAllPermissions();


    public List<Menu>  selectMenuByAdminId(Integer uid);

    /**
     * 根据父id查询
     * @param parentid
     * @return
     */
    public List<Menu> queryByParentId(Integer parentid);

    /**
     * 根据父id查询
     * @param menuLists
     * @param parentId
     * @return
     */
    public List<Menu> getChildMenuList(ArrayList<Menu> menuLists, Integer parentId);

    public Menu selectById(Integer id);

    public void save(Menu menu);

    public void saveRoleMenu(Integer roleId, Integer[] menuIds);

    public void saveRoleMenuFlag(Integer roleId, Integer[] menuIds);

    public void delRoleMenu(Integer roleId);

    public void update(Menu menu);

    public void saveOrUpdate(Menu menu);

    public void delMenu(Integer[] ids);

    public List<SysRoleMenu> selectByRoleId(Integer roleId);
}
