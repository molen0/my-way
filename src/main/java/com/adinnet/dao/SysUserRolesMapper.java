package com.adinnet.dao;

import com.adinnet.repository.SysRole;
import com.adinnet.repository.SysUserRole;
import com.adinnet.repository.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;


/**
 * @author wangren
 * @Description: 用户角色
 * @create 2018-09-13 15:42
 **/
public interface SysUserRolesMapper extends JpaRepository<SysUserRole, Integer>, JpaSpecificationExecutor<String> {

    @Query("from SysUserRole p where p.adminId = :adminId")
    public List<SysUserRole> queryByUserId(@Param(value = "adminId") Integer adminId);

    @Query(value = "from SysUserRole sur LEFT JOIN SysRole sr ON sr.id=sur.sysRole.id where sur.adminId = :adminId")
    public List<SysRole> findByAdminId(@Param(value = "adminId") Integer adminId);

    @Query("update  SysUserRole p set p.isDelete =0 where p.adminId = :adminId")
    @Modifying
    public void delAllBeUid(@Param(value = "adminId") Integer adminId);

    @Query(value = "delete from admin_role   where role_id = :roleId",nativeQuery = true)
    @Modifying
    @Transactional
    public void delAllByRoleId(@Param(value = "roleId") Integer roleId);
}
