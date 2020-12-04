package com.icecream.controller;

import com.icecream.enums.Roles;
import com.icecream.model.ResultMap;
import com.icecream.service.UsersService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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

    @PostMapping("/login")
    public void login(@RequestParam("username") String username,
                      @RequestParam("password") String password,
                      HttpServletRequest request,
                      HttpServletResponse response) throws IOException {

        // 从 SecurityUtils 里创建一个 subject
        Subject subject = SecurityUtils.getSubject();
        // 在提交认证前准备一个 token（令牌）
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        // 执行认证登录
        subject.login(token);

        // 根据权限返回指定数据
        String role = usersService.getRole(username);

        if (Roles.NORMAL.getRoles().equals(role)) {
            response.sendRedirect("111");
        }
        if (Roles.ADMIN.getRoles().equals(role)) {
            response.sendRedirect(request.getContextPath() + "/admin/getMessage");
        }
    }
}
