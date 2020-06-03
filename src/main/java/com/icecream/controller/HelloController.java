package com.icecream.controller;

import com.icecream.entity.Users;
import com.icecream.service.UsersService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@ComponentScan("com.icecream")
@MapperScan("com.icecream.mapper")
public class HelloController {

    @Autowired
    private UsersService usersService;

    @RequestMapping("/index")
    @ResponseBody
    public List index() {
        List<Users> listUsers = usersService.getAll();
        return listUsers;
    }
}
