package com.icecream.config;

import com.icecream.filter.JWTFilter;
import com.icecream.shiro.CustomRealm;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {

    @Bean
    public ShiroFilterFactoryBean shirFilter(SecurityManager securityManager) {

        ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();

        // 添加自己的过滤器
        Map<String, Filter> filterMap = new LinkedHashMap<>();

        // 设置自定义的JWT过滤器
        filterMap.put("jwt", new JWTFilter());
        factoryBean.setFilters(filterMap);
        factoryBean.setSecurityManager(securityManager);

        // 设置无权限时跳转的url
        factoryBean.setUnauthorizedUrl("/unauthorized/无权限");
        Map<String, String> filterRuleMap = new HashMap<>();
        // 所有请求通过自定义的JWTFilter
        filterRuleMap.put("/**", "jwt");
        // 访问unauthorized/** 不通过JWTFilter
        filterRuleMap.put("/unauthorized/**", "anon");
        factoryBean.setFilterChainDefinitionMap(filterRuleMap);
        return factoryBean;


//        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
//        // 必须设置 SercurityManager
//        shiroFilterFactoryBean.setSecurityManager(securityManager);
//        // 如果不设置，则会默认寻找Web工程根目录下的 "/login.jsp" 或 "/login"映射
//        shiroFilterFactoryBean.setLoginUrl("/start");
//        // 设置无权限时跳转的 Url
//        shiroFilterFactoryBean.setUnauthorizedUrl("/notRole");
//
//        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
//        // 游客
//        filterChainDefinitionMap.put("/guest/**", "anon");
//        // 用户, 需要角色权限 "user"
//        filterChainDefinitionMap.put("/user/**", "roles[user]");
//        // 管理员
//        filterChainDefinitionMap.put("/admin/**", "roles[admin]");
//        // 开放登录接口
//        filterChainDefinitionMap.put("/login", "anon");
//        // 其余接口一律拦截
//        filterChainDefinitionMap.put("/**", "authc");
//
//        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
//        return shiroFilterFactoryBean;
    }

    /**
     * 注入 securityManager
     * @return
     */
    @Bean
    public SecurityManager securityManager(CustomRealm customRealm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();

        // 关闭shiro自带的session
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
        subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
        securityManager.setSubjectDAO(subjectDAO);
//        // 设置 Realm
//        securityManager.setRealm(customRealm);
        return securityManager;
    }

    /**
     * 自定义身份认证 realm
     * 注入 customRealm, 必须加 @Bean 注解，否则影响 CustomRealm 类中其他类的依赖注入
     * @return
     */
//    @Bean
//    public  CustomRealm customRealm() {
//        return new CustomRealm();
//    }

    /**
     * 添加注解支持
     * @return
     */
    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        // 强制使用cglib,防止重复代理和可能引起代理出错的问题
        defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
        return defaultAdvisorAutoProxyCreator;
    }

    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }
}
