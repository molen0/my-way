package adinnet.controller.api;

import com.adinnet.repository.Menu;
import com.adinnet.service.SysRolesMenuService;
import com.adinnet.utils.ReturnUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
 * @Description: 菜单管理
 * @create 2018-09-14 13:37
 **/
@Controller
@RequestMapping("/api/menu")
@Slf4j
public class MenuController {

    @Autowired
    private SysRolesMenuService sysRolesMenuService;

    @RequestMapping(value = "/index", method = {RequestMethod.GET})
    public String index(Model model,Menu menu) {
        ArrayList<Menu> menuLists = new ArrayList<>();
        List<Menu> Lists = sysRolesMenuService.getChildMenuList(menuLists,0);
        model.addAttribute("menus", Lists);
        return "menu/index";
    }

    @RequestMapping(value = "/from", method = {RequestMethod.GET})
    public String add(Menu menu, Model model) {
        if(null != menu.getMenuId()){
            menu = sysRolesMenuService.selectById(menu.getMenuId());
        }else if( null != menu.getParentId()){
            menu.setParentId(menu.getParentId());
            menu.setMenuType("auth");
        }else{
            menu.setParentId(0);
            menu.setMenuType("auth");
        }
        model.addAttribute("menu", menu);

        return "menu/from";
    }

    @RequestMapping(value = "/save", method = {RequestMethod.POST})
    @ResponseBody
    public ModelMap save( Menu menu) {
        try {
            sysRolesMenuService.saveOrUpdate(menu);
            log.info(menu.toString());
            return ReturnUtil.Success("操作成功", null, "/api/menu/index");
        } catch (Exception e) {
            e.printStackTrace();
            return ReturnUtil.Error("操作失败", null, null);
        }
    }

    @RequestMapping(value = "/delete", method = {RequestMethod.GET})
    @ResponseBody
    public ModelMap delete(Integer[] ids) {
        try {
            sysRolesMenuService.delMenu(ids);
            return ReturnUtil.Success("操作成功", null, null);
        } catch (Exception e) {
            e.printStackTrace();
            return ReturnUtil.Error("操作失败", null, null);
        }
    }
}
