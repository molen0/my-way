package com.adinnet.dao;

import com.adinnet.repository.SysUserRoleSave;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author wangren
 * @Description: 用户角色保存
 * @create 2018-09-19 10:48
 **/
public interface SysUserRoleSaveMapper extends JpaRepository<SysUserRoleSave, Integer> {
}
