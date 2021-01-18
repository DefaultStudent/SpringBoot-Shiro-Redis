package com.icecream.controller;

import com.icecream.model.ResultMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/guest")
public class GuestController {

    private final ResultMap resultMap;

    @Autowired
    public GuestController (ResultMap resultMap) {
        this.resultMap = resultMap;
    }

    @RequestMapping(value = "/enter", method = RequestMethod.GET)
    public ResultMap login() {
        return resultMap.success().message("欢迎进入，身份为游客");
    }

    @RequestMapping(value = "/getMessage", method = RequestMethod.GET)
    public ResultMap submitLogin() {
        return resultMap.success().code(200).message("游客您好，您拥有获得该接口的信息的权限");
    }
}
