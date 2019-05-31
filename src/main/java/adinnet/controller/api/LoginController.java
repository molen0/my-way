package adinnet.controller.api;

import com.adinnet.common.CustomerAuthenticationToken;
import com.adinnet.common.LoginEnum;
import com.adinnet.common.MenuTree;
import com.adinnet.repository.Menu;
import com.adinnet.repository.UserInfo;
import com.adinnet.repository.ValidAdmin;
import com.adinnet.service.SysRolesMenuService;
import com.adinnet.service.SysUserService;
import com.adinnet.utils.IpUtil;
import com.adinnet.utils.PasswordUtil;
import com.adinnet.utils.ShiroUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;


/**
 * @author wangren
 * @Description: 测试controller
 * @create 2018-09-13 14:02
 **/
@Controller
@Slf4j
public class LoginController {

    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysRolesMenuService sysRolesMenuService;

    @RequestMapping("/")
    public String execute() {
        return "login";
    }

    @RequestMapping("/api/index")
    public String login(Model model) {
        UserInfo admin = ShiroUtil.getUserInfo();
        List<Menu> menuList = null;
        if(admin.getIsSystem() == 1){
            menuList = sysRolesMenuService.queryAllPermissions();
        }else{
            menuList = sysRolesMenuService.selectMenuByAdminId(admin.getUid());
        }
        MenuTree menuTreeUtil = new MenuTree(menuList,null);
        model.addAttribute("menuLists", menuTreeUtil.buildTreeGrid());
        model.addAttribute("admin",admin);
        return "index";
    }
    @RequestMapping(value = "/api/main", method = {RequestMethod.GET})
    public String right(Model model) {
       // model.addAllAttributes(this.getTotal());
        return "right";
    }

    @RequestMapping(value="/login", method= RequestMethod.POST)
    public String loginPost(@Valid ValidAdmin validAdmin, BindingResult bindingResult, RedirectAttributes redirectAttributes, HttpServletRequest request,Model model){
        if(bindingResult.hasErrors()){
            return "redirect:/";
        }
        String username = validAdmin.getUsername();
        UserInfo userInfo = sysUserService.queryByUserName(username);
        if(null != userInfo){
            validAdmin.setPassword(PasswordUtil.createAdminPwd(validAdmin.getPassword(), userInfo.getCredentialsSalt()));
        }
        Subject subject = SecurityUtils.getSubject();
        //UsernamePasswordToken token = new UsernamePasswordToken(validAdmin.getUsername(), validAdmin.getPassword());
        CustomerAuthenticationToken token = new CustomerAuthenticationToken(validAdmin.getUsername(), validAdmin.getPassword(), false);
        token.setLoginType(LoginEnum.ADMIN.toString());
        //获取当前的Subject
        Subject currentUser = SecurityUtils.getSubject();
        try {
            //在调用了login方法后,SecurityManager会收到AuthenticationToken,并将其发送给已配置的Realm执行必须的认证检查
            //每个Realm都能在必要时对提交的AuthenticationTokens作出反应
            //所以这一步在调用login(token)方法时,它会走到MyRealm.doGetAuthenticationInfo()方法中,具体验证方式详见此方法
            log.info("对用户[" + username + "]进行登录验证..验证开始");
            currentUser.login(token);
            log.info("对用户[" + username + "]进行登录验证..验证通过");
        }catch(UnknownAccountException uae){
            log.info("对用户[" + username + "]进行登录验证..验证未通过,未知账户");
            redirectAttributes.addFlashAttribute("message", "未知账户");
        }catch(IncorrectCredentialsException ice){
            log.info("对用户[" + username + "]进行登录验证..验证未通过,错误的凭证");
            redirectAttributes.addFlashAttribute("message", "密码不正确");
        }catch(LockedAccountException lae){
            log.info("对用户[" + username + "]进行登录验证..验证未通过,账户已锁定");
            redirectAttributes.addFlashAttribute("message", "账户已锁定");
        }catch(ExcessiveAttemptsException eae){
            log.info("对用户[" + username + "]进行登录验证..验证未通过,错误次数过多");
            redirectAttributes.addFlashAttribute("message", "用户名或密码错误次数过多");
        }catch(AuthenticationException ae){
            //通过处理Shiro的运行时AuthenticationException就可以控制用户登录失败或密码错误时的情景
            log.info("对用户[" + username + "]进行登录验证..验证未通过,堆栈轨迹如下");
            ae.printStackTrace();
            redirectAttributes.addFlashAttribute("message", "用户名或密码不正确");
        }
        //验证是否登录成功
        if(currentUser.isAuthenticated()){
            Session session = SecurityUtils.getSubject().getSession();
            session.setAttribute("loginType",LoginEnum.ADMIN.toString());
            log.info("用户[" + username + "]登录认证通过(这里可以进行一些认证通过后的一些系统参数初始化操作)");
            String ip = IpUtil.getIpAddr(request);
            //logService.insertLoginLog(username, ip, request.getContextPath());
            return "redirect:/api/index";
        }else{
            token.clear();
            return "redirect:/";
        }
    }

    @RequestMapping(value="/logout",method=RequestMethod.GET)
    public String logout(RedirectAttributes redirectAttributes ){
        //使用权限管理工具进行用户的退出，跳出登录，给出提示信息
        System.out.println("PublicController.logout()");
        SecurityUtils.getSubject().logout();
        redirectAttributes.addFlashAttribute("message", "您已安全退出");
        return "redirect:/";
    }
}