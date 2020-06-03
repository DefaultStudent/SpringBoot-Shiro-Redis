package com.icecream.service;


import com.icecream.entity.Users;

import java.util.List;

public interface UsersService {

    /**
     * 获取全部用户信息
     * @return
     */
    List<Users> getAll();

    /**
     * 用户登录
     * @param id
     * @param pwd
     * @return
     */
    Users loginPage(String id, String pwd);
}
