package com.icecream.controller;

import com.icecream.entity.Users;
import com.icecream.model.ResultMap;
import com.icecream.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final ResultMap resultMap;

    private final UsersService usersService;

    @Autowired
    public AdminController(ResultMap resultMap, UsersService usersService){
        this.resultMap = resultMap;
        this.usersService = usersService;
    }

    @GetMapping("/getMessage")
    @RequiresRoles("admin")
    public ResultMap getMessage() {
        List<Users> list = usersService.getAll();
        return resultMap.success().message("您拥有管理员权限");
    }

    @GetMapping("/showIndex.html")
    @RequiresRoles("admin")
    private ModelAndView showIndex() {
        List<Users> list = usersService.getAll();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index");
        return modelAndView;
    }

    @GetMapping("/welcome")
    private ModelAndView showWelcome() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("welcome");
        return modelAndView;
    }
}
