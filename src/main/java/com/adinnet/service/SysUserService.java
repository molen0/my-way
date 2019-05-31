package com.adinnet.service;

import com.adinnet.repository.UserInfo;
import org.springframework.data.domain.Page;
import org.springframework.ui.ModelMap;

import java.util.List;

/**
 * @author wangren
 * @Description: 用户service
 * @create 2018-09-13 15:22
 **/
public interface SysUserService {
   /**
    * 根据登录名查询
    * @param username
    * @return
    */
   public UserInfo queryByUserName(String username);

   /**
    * 分页查询
    * @param userInfo
    * @return
    */
   public Page<UserInfo> pageList(UserInfo userInfo);

   /**
    * 根据主键查询用户信息
    * @param uid
    * @return
    */
   public UserInfo queryById(Integer uid);


   public void save(UserInfo userInfo);

   public ModelMap saveOrUpdate(UserInfo userInfo);

   public void update(UserInfo userInfo);

   public void del(Integer uid);
   public void updatepwd(UserInfo userInfo) ;
}
