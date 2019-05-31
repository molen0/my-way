package com.adinnet.service;

import com.adinnet.repository.SysRole;
import com.adinnet.repository.SysUserRole;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @author wangren
 * @Description: 用户角色
 * @create 2018-09-13 15:23
 **/
public interface SysUserRolesService {
   /**
    * 根据用户id查询所有的角色
    * @param adminId
    * @return
    */
   public List<SysUserRole> queryByUserId(Integer adminId);

   /**
    * 根据用户id查询所有的角色
    * @param adminId
    * @return
    */
   public List<SysRole> selectRoleListByAdminId(Integer adminId);

   /**
    * 查询所有的角色
    * @return
    */
   public List<SysRole> selectAll();

   /**
    * 分页查询
    * @param sysRole
    * @return
    */
   public Page<SysRole> pageList(SysRole sysRole);

   /**
    * 根据主键id
    * 查询
    * @param id
    * @return
    */
   public SysRole getOne(Integer id);
   /**
    * 保存用户角色
    * @param sysUserRole
    */
   public void save(SysUserRole sysUserRole);

   public void saveRole(SysRole sysRole);

   public void updateRole(SysRole sysRole);

   public void saveOrUpDateRole(SysRole sysRole);

   /**
    * 删除所有用户角色
    * @param adminId
    */
   public void delAllByUid(Integer adminId);

   public void delRoles(Integer[] ids);
}
