package com.icecream.controller;

import com.icecream.model.ResultMap;
import com.icecream.service.UsersService;
import com.icecream.utils.Constant;
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
import java.io.UnsupportedEncodingException;

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

    @GetMapping(value = "/logout")
    public void logout(HttpServletRequest request,
                            HttpServletResponse response) throws IOException{

        Object user_session = request.getSession().getAttribute(Constant.USER_SESSION);

        if (user_session != null) {
            request.getSession().removeAttribute(Constant.USER_SESSION);
            request.getSession().removeAttribute(Constant.USER_NAME);
            response.sendRedirect(request.getContextPath() + "/start");
        }

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

        String realPassword = usersService.getPassword(username);

        if (realPassword == null || !realPassword.equals(password))response.sendRedirect(request.getContextPath() + "/start");

        //Authorization
        request.getSession().setAttribute(Constant.USER_SESSION, request.getSession().getId());
        request.getSession().setAttribute(Constant.USER_NAME, username);

        String role = usersService.getRole(username);

        if ("admin".equals(role)) response.sendRedirect(request.getContextPath() + "/admin/showIndex.html");
        if ("user".equals(role)) response.sendRedirect(request.getContextPath() + "/user/getMessage");


    }

    @RequestMapping(path = "/unauthorized/{message}")
    public ResultMap unauthorized(@PathVariable String message) throws UnsupportedEncodingException {
        return resultMap.success().code(401).message(message);
    }
}
