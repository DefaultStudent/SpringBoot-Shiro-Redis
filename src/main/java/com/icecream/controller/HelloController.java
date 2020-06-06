package com.icecream.controller;

import com.icecream.entity.Users;
import com.icecream.model.ResultMap;
import com.icecream.service.UsersService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * @author 96495
 */
@RestController
@ComponentScan("com.icecream")
@MapperScan("com.icecream.mapper")
public class HelloController {

    private final UsersService usersService;

    private final ResultMap resultMap;

    @Autowired
    public HelloController(ResultMap resultMap, UsersService usersService) {
        this.resultMap = resultMap;
        this.usersService = usersService;
    }

    @RequestMapping(value = "/notLogin", method = RequestMethod.GET)
    public ResultMap notLogin() {
        return resultMap.success().message("您尚未登录");
    }

    @RequestMapping(value = "/notRole", method = RequestMethod.GET)
    public ResultMap notRole() {
        return resultMap.success().message("您没有权限");
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public ResultMap logout() {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return resultMap.success().message("注销成功");
    }

    /**
     * 进入登陆页面
     * @return
     */
    @RequestMapping("start")
    private ModelAndView start() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        return modelAndView;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResultMap login(String username, String userpassword) {
        // 从 SecurityUtils 里创建一个 subject
        Subject subject = SecurityUtils.getSubject();
        // 在提交认证前准备一个 token（令牌）
        UsernamePasswordToken token = new UsernamePasswordToken(username, userpassword);
        // 执行认证登录
        subject.login(token);

        // 根据权限返回指定数据
        String role = usersService.getRole(username);

        if ("user".equals(role)) {
            return resultMap.success().message("欢迎登录");
        }
        if ("admin".equals(role)) {
            return resultMap.success().message("欢迎来到管理员权限页面");
        }
        return resultMap.fail().message("权限错误");
    }
}
