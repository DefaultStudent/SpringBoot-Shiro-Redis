package com.icecream.controller;

import com.icecream.model.ResultMap;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author 96495
 * @date 2020/6/6
 *
 */
@RestController
@RequestMapping("/user")
public class UserController {

    private final ResultMap resultMap;

    @Autowired
    public UserController(ResultMap resultMap) {
        this.resultMap = resultMap;
    }

    @GetMapping(value = "/getMessage")
    @RequiresRoles("user")
    public ResultMap getMessage() {
        return resultMap.success().message("你拥有用户权限，可以获得该接口信息！");
    }
}
