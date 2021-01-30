package com.icecream.shiro;

import com.icecream.service.UsersService;
import com.icecream.utils.JWTUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * @author 96495
 */
@Component
public class CustomRealm extends AuthorizingRealm {

    private UsersService usersService;
    private  Logger logger;

    /**
     * 不在这里使用 @Lazy 注解的话，会导致Spring自带的 @Cacheable 注解失效，无法放入 Redis 缓存中
     * @param usersService
     */
    @Autowired
    @Lazy
    public void CustomRealm(UsersService usersService) {
        this.usersService = usersService;
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JWTToken;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {

        String token = (String) authenticationToken.getCredentials();

        // 解密获得username，用于和数据库进行对比
        String username = JWTUtil.getUsername(token);
        if (username == null || !JWTUtil.verify(token, username)) {
            throw new AuthenticationException("token认证失败");
        }

        String password = usersService.getPassword(username);
        if (password == null) throw new AuthenticationException("该用户不存在");

        return new SimpleAuthenticationInfo(token, token, "MyRealm");
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String username = JWTUtil.getUsername(principalCollection.toString());
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

        String role = usersService.getRole(username);
        String rolePermission = usersService.getRolePermission(username);
        String permission = usersService.getPermission(username);

        Set<String> roleSet = new HashSet<>();
        Set<String> permissionSet = new HashSet<>();

        roleSet.add(role);
        permissionSet.add(rolePermission);
        permissionSet.add(permission);

        info.setRoles(roleSet);
        info.setStringPermissions(permissionSet);
        return info;
    }

//    /**
//     * 获取身份验证信息
//     * Shiro 中，最终是通过 Realm 来获取应用程序中的用户、角色及权限信息的
//     *
//     * @param authenticationToken 用户身份信息 token
//     * @return 封装了用户信息的 AuthenticationInfo 实例
//     * @throws AuthenticationException
//     */
//    @Override
//    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
//        UsernamePasswordToken token = (UsernamePasswordToken)authenticationToken;
//
//        // 解密获得 username, 用于和数据库进行对比
//        String password = usersService.getPassword(token.getUsername());
//
//        if (null == password) {
//            throw new AccountException("用户名不正确");
//        }
//
//        return new SimpleAuthenticationInfo(token.getPrincipal(), password, getName());
//    }

//    /**
//     * 获取授权信息
//     *
//     * @param principalCollection
//     * @return
//     */
//    @Override
//    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
//        // 权限认证
//        String username = (String) SecurityUtils.getSubject().getPrincipal();
//        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
//        // 获得该用户权限
//        String role = usersService.getRole(username);
//        Set<String> set = new HashSet<>();
//        // 需要讲role封装到set作为info.setRoles()的参数
//        set.add(role);
//        // 设置该用户拥有的角色
//        info.setRoles(set);
//        return info;
//    }
}
