package com.icecream.controller;

import com.icecream.entity.Users;
import com.icecream.model.ResultMap;
import com.icecream.service.UsersService;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
}
