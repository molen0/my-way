package com.adinnet.controller.api;

import com.adinnet.common.MenuTree;
import com.adinnet.repository.Menu;
import com.adinnet.repository.SysRole;
import com.adinnet.repository.SysRoleMenu;
import com.adinnet.service.SysRolesMenuService;
import com.adinnet.service.SysUserRolesService;
import com.adinnet.utils.ReturnUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
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
import java.util.Map;


/**
 * @author wangren
 * @Description: 角色管理
 * @create 2018-09-14 13:36
 **/
@Controller
@RequestMapping("/api/role")
public class RoleController {

    @Autowired

    private SysUserRolesService sysUserRolesService;
    @Autowired
    private SysRolesMenuService sysRolesMenuService;
    @RequestMapping(value = "/index", method = {RequestMethod.GET})
    public String index(Model model) {
        return "role/index";
    }

    @RequestMapping(value = "/list", method = {RequestMethod.GET})
    @ResponseBody
    public ModelMap list(SysRole role) {
        ModelMap map = new ModelMap();
        Page<SysRole> page = sysUserRolesService.pageList(role);
        map.put("pageInfo", page);
        map.put("queryParam", role);
        return ReturnUtil.Success("加载成功", map, null);
    }

    @RequestMapping(value = "/from", method = {RequestMethod.GET})
    public String from(SysRole role, Model model) {
        if (null != role.getId()) {
            role = sysUserRolesService.getOne(role.getId());
        }
        model.addAttribute("role", role);
        return "role/from";
    }

    @RequestMapping(value = "/save", method = {RequestMethod.POST})
    @ResponseBody
    public ModelMap save( SysRole role, BindingResult result) {
        if (result.hasErrors()) {
            for (ObjectError er : result.getAllErrors()) return ReturnUtil.Error(er.getDefaultMessage(), null, null);
        }
        try {
                sysUserRolesService.saveOrUpDateRole(role);
            return ReturnUtil.Success("操作成功", null, "/api/role/index");
        } catch (Exception e) {
            e.printStackTrace();
            return ReturnUtil.Error("操作失败", null, null);
        }
    }

    @RequestMapping(value = "/grant", method = {RequestMethod.POST})
    @ResponseBody
    public ModelMap grant(Integer roleId, Integer[] menuIds) {
        try {
            sysRolesMenuService.saveRoleMenuFlag(roleId,menuIds);
            return ReturnUtil.Success("操作成功", null, null);
        } catch (Exception e) {
            e.printStackTrace();
            return ReturnUtil.Error("操作失败", null, null);
        }
    }

    @RequestMapping(value = "/grant", method = {RequestMethod.GET})
    public String grantForm(Integer id, Model model) {
        model.addAttribute("roleId", id);
        return "role/grant";
    }
    @RequestMapping(value = "/menutree", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public ModelMap comboTree(Integer id) {
        List<Menu> menuList = sysRolesMenuService.queryAllPermissions();
        List<SysRoleMenu> roleMenuLists = sysRolesMenuService.selectByRoleId(id);
        MenuTree menuTreeUtil = new MenuTree(menuList, roleMenuLists);
        List<Map<String, Object>> mapList = menuTreeUtil.buildTree();
        return ReturnUtil.Success(null, mapList, null);
    }
    @RequestMapping(value = "/delete", method = {RequestMethod.GET})
    @ResponseBody
    public ModelMap delete(Integer[] ids) {
        try {
            if ("null".equals(ids) || "".equals(ids)) {
                return ReturnUtil.Error("Error", null, null);
            } else {
                sysUserRolesService.delRoles(ids);
                return ReturnUtil.Success("操作成功", null, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ReturnUtil.Error("操作失败", null, null);
        }
    }
}
