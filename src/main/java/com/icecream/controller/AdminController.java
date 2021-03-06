package com.icecream.controller;

import com.icecream.entity.Users;
import com.icecream.entity.WeaponKind;
import com.icecream.model.ResultMap;
import com.icecream.service.UsersService;
import com.icecream.service.WeaponKindService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final ResultMap resultMap;

    private final UsersService usersService;

    private final WeaponKindService weaponKindService;

    Logger logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);

    @Autowired
    public AdminController(ResultMap resultMap, UsersService usersService, WeaponKindService weaponKindService){
        this.resultMap = resultMap;
        this.usersService = usersService;
        this.weaponKindService = weaponKindService;
    }

    @GetMapping("/getMessage")
    @RequiresRoles("admin")
    public ResultMap getMessage() {
        logger.info("您拥有管理员权限");
        List<Users> list = usersService.getAll();
        return resultMap.success().code(200).message("您拥有管理员权限");
    }

    @GetMapping("/showIndex.html")
    @RequiresRoles("admin")
    public ModelAndView showIndex() {
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

    @GetMapping ("/getAllWeapons")
    public List<WeaponKind> index() {
        ArrayList<WeaponKind> kindList = weaponKindService.getAllWeapon();

        return kindList;
    }
}
