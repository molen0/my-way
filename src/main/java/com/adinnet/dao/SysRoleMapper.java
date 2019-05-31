package com.adinnet.dao;

import com.adinnet.repository.SysRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author wangren
 * @Description: 角色
 * @create 2018-09-17 11:31
 **/
public interface SysRoleMapper extends JpaRepository<SysRole, Integer>, JpaSpecificationExecutor<SysRole> {
}
