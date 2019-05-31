package adinnet.config;

import com.adinnet.repository.Menu;
import com.adinnet.repository.SysUserRole;
import com.adinnet.repository.UserInfo;
import com.adinnet.service.SysRolesMenuService;
import com.adinnet.service.SysUserRolesService;
import com.adinnet.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @author wangren
 * @Description: 授权配置
 * @create 2018-09-13 15:19
 **/
@Slf4j
public class UserRealm extends AuthorizingRealm {
    @Autowired
    private SysUserService userService;
    @Autowired
    private SysUserRolesService userRolesService;
    @Autowired
    private SysRolesMenuService rolesPermissionsService;

    /**
     * 实现授权
     * @param principalCollection 验证信息
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        //给资源进行授权
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        //授权字符串数组
        Set<String> permissions = new HashSet<String>();
        //通过SecurityUtils获取subject再获取userBean
        Subject subject = SecurityUtils.getSubject();
        UserInfo userBean = (UserInfo) subject.getPrincipal();//这里的返回值是SimpleAuthenticationInfo里的principal

        List<SysUserRole> userRolesBeans = userRolesService.queryByUserId(userBean.getUid());
        Set<Integer> roles = new HashSet<Integer>();

        for(int i=0;i<userRolesBeans.size();i++){
            roles.add(userRolesBeans.get(i).getSysRole().getId());
        }
        Iterable<Menu> set = rolesPermissionsService.queryPermissionsByRoles(roles);
        Iterator<Menu> iter = set.iterator();
        while(iter.hasNext()){
            Menu menu = (Menu) iter.next();
            permissions.add(menu.getMenuUrl());
        }
        //permissions = rolesPermissionsService.queryPermissionsByRoles(roles);
//        System.out.println(permissions);

        //添加授权字符串
        info.addStringPermissions(permissions);
//        info.addStringPermission("user:add");
        return info;
    }

    /**
     * 实现验证
     * @param authenticationToken  主体传递过来的信息
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
//        String userName = "jerrrylin";
//        String password =  "123";
        //获取passwordToken
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        //获取username
        String username = token.getUsername();

        UserInfo userBean = userService.queryByUserName(username);


        if(userBean==null){
            return null;//shiro底层抛出UnknownAccountException
        }
        log.info("salt====="+userBean.getCredentialsSalt());
        //加密方式;
        //交给AuthenticatingRealm使用CredentialsMatcher进行密码匹配，如果觉得人家的不好可以自定义实现
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                userBean, //用户名
                userBean.getPassword(), //密码
              //  ByteSource.Util.bytes(userBean.getCredentialsSalt()),//salt=username+salt
                userBean.getUsername()  //realm name
        );
        //密码判断 第一个参数是返回信息   principal   password  realmName
        return authenticationInfo;
    }
}
