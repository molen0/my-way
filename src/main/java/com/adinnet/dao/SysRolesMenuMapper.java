package com.adinnet.dao;

import com.adinnet.repository.Menu;
import com.adinnet.repository.SysRoleMenu;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author wangren
 * @Description: 角色权限
 * @create 2018-09-13 15:41
 **/
public interface SysRolesMenuMapper extends PagingAndSortingRepository<SysRoleMenu, Integer>, JpaSpecificationExecutor<SysRoleMenu> {

    @Query("delete from SysRoleMenu where roleId = :roleId")
    @Modifying
    public void deleteSysRoleMenu(@Param("roleId") Integer roleId);

}
