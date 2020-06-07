package com.icecream.shiro;

import com.icecream.service.UsersService;
import com.icecream.utils.JWTUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * @author 96495
 */
@Component
public class CustomRealm extends AuthorizingRealm {

    private UsersService usersService;

    @Autowired
    private void setUsersService(UsersService usersService) {
        this.usersService = usersService;
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof  JWTToken;
    }

    /**
     * 获取身份验证信息
     * Shiro 中，最终是通过 Realm 来获取应用程序中的用户、角色及权限信息的
     *
     * @param authenticationToken 用户身份信息 token
     * @return 封装了用户信息的 AuthenticationInfo 实例
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String token = (String) authenticationToken.getCredentials();

        // 解密获得 username, 用于和数据库进行对比
        String username = JWTUtil.getUsername(token);
        if ( username == null || !JWTUtil.verify(token, username)) {
            throw new AuthenticationException("token认证失败");
        }

        // 从数据库获取对应用户名用户的密码
        String password= usersService.getPassword(username);
        if (null == password) {
            throw new AccountException("用户名不正确");
        }

        int ban = usersService.checkUserBanStatus(username);
        if (ban == 1) {
            throw new AuthenticationException("给用户已被封禁");
        }
        return new SimpleAuthenticationInfo(token, token, getName());
    }

    /**
     * 获取授权信息
     *
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String username = JWTUtil.getUsername(principalCollection.toString());
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        // 获得该用户的角色
        String role = usersService.getRole(username);
        // 每个角色拥有的默认的权限
        String rolePermission = usersService.getRolePermission(username);
        // 每个用户可以设置新的权限
        String permission = usersService.getPermission(username);
        Set<String> roleSet = new HashSet<>();
        Set<String> permissionSet = new HashSet<>();
        // 需要将 role, permission 封装到 Set 作为 info.setRoles(), info.setStringPermissions()的参数
        roleSet.add(role);
        permissionSet.add(rolePermission);
        permissionSet.add(permission);
        info.setRoles(roleSet);
        info.setStringPermissions(permissionSet);
        return info;
    }
}
