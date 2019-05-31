package com.adinnet.controller.api;

import com.adinnet.repository.SysRole;
import com.adinnet.repository.SysUserRole;
import com.adinnet.repository.UserInfo;
import com.adinnet.service.SysUserRolesService;
import com.adinnet.service.SysUserService;
import com.adinnet.utils.PasswordUtil;
import com.adinnet.utils.ReturnUtil;
import com.adinnet.utils.ShiroUtil;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wangren
 * @Description: 管理员
 * @create 2018-09-14 13:34
 **/
@Controller
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysUserRolesService sysUserRolesService;
    @RequestMapping(value = "/index", method = {RequestMethod.GET})
    public String index(Model model) {
        return "admin/index";
    }

    @RequestMapping(value = "/list", method = {RequestMethod.GET})
    @ResponseBody
    public ModelMap list(UserInfo userInfo,String username) {
        ModelMap map = new ModelMap();
       // map.put("pageInfo", new PageInfo<UserInfo>(sysUserService.getPageList(userInfo).getContent()));
        Page<UserInfo> page= sysUserService.pageList(userInfo);
        map.put("pageInfo", page);
        map.put("queryParam", userInfo);
        return ReturnUtil.Success("加载成功", map, null);
    }

    @RequestMapping(value = "/from", method = {RequestMethod.GET})
    public String from(UserInfo userInfo, Model model) {
        String checkRoleId = "";
        if(null != userInfo.getUid() ){
            userInfo = sysUserService.queryById(userInfo.getUid());
            List<SysUserRole> sysUserRoles = sysUserRolesService.queryByUserId(userInfo.getUid());
            ArrayList<Integer> checkRoleIds = new ArrayList<Integer>();
            for(SysUserRole sysUserRole : sysUserRoles){
                checkRoleIds.add(sysUserRole.getSysRole().getId());
            }
            checkRoleId = String.join(",", checkRoleIds.toString());
        }else {
            userInfo.setIsSystem(0);
        }
        model.addAttribute("checkRoleId", checkRoleId);
        model.addAttribute("roleLists", this.getRoleList());
        model.addAttribute("admin", userInfo);
        return "admin/from";
    }

    @RequestMapping(value = "/user", method = {RequestMethod.GET})
    public String updateUser( Model model) {
        UserInfo admin = ShiroUtil.getUserInfo();
        model.addAttribute("admin", admin);
        return "admin/user";
    }

    private List<SysRole> getRoleList() {
        ModelMap map = new ModelMap();
        List<SysRole> roleList = sysUserRolesService.selectAll();
        return roleList;
    }
    @RequestMapping(value = "/savepwd", method = {RequestMethod.POST})
    @ResponseBody
    public ModelMap editPwd(String password ,String password1 ,String password2) {
        try {
            if ( StringUtils.isNotEmpty(password) && StringUtils.isNotEmpty(password1)&& StringUtils.isNotEmpty(password2)) {
                UserInfo admin = ShiroUtil.getUserInfo();
                if(password1.equals(password2)){
                    String oldPassword = PasswordUtil.createAdminPwd(password, admin.getCredentialsSalt());
                    String newPassword = PasswordUtil.createAdminPwd(password1, admin.getCredentialsSalt());
                    if(admin.getPassword().equals(oldPassword)){
                        admin.setPassword(newPassword);
                        sysUserService.updatepwd(admin);
                        return ReturnUtil.Success("操作成功", null, null);
                     }else{
                        return ReturnUtil.Error("旧密码不正确，修改失败", null, null);
                    }
                }else{
                    return ReturnUtil.Error("两次密码不一致，修改失败", null, null);
                }
            } else {
                return ReturnUtil.Error("参数错误，修改失败", null, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ReturnUtil.Error("修改失败", null, null);
        }
    }
    @RequestMapping(value = "/save", method = {RequestMethod.POST})
    @ResponseBody
    public ModelMap save( UserInfo userInfo, BindingResult result) {
        try {
            if (result.hasErrors()) {
                for (ObjectError er : result.getAllErrors())
                    return ReturnUtil.Error(er.getDefaultMessage(), null, null);
            }
            return sysUserService.saveOrUpdate(userInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return ReturnUtil.Error("操作失败", null, null);
        }
    }

    @RequestMapping(value = "/delete", method = {RequestMethod.GET})
    @ResponseBody
    public ModelMap delete(Integer[] ids) {
        try {
            if (ids != null) {
                if (StringUtils.isNotBlank(ids.toString())) {
                    for (Integer id : ids) {
                        sysUserService.del(id);
                    }
                }
                return ReturnUtil.Success("删除成功", null, null);
            } else {
                return ReturnUtil.Error("删除失败", null, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ReturnUtil.Error("删除失败", null, null);
        }
    }
}
