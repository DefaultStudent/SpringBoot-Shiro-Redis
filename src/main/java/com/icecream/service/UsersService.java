package com.icecream.service;


import com.icecream.entity.Users;
import org.springframework.cache.annotation.Cacheable;

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

    /**
     * 获取用户密码
     * @param username 用户名
     * @return
     */
    String getPassword(String username);

    /**
     * 获取用户权限
     * @param username 用户名
     * @return
     */
    String getRole(String username);
}
