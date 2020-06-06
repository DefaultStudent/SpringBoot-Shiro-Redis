package com.icecream.service.impl;

import com.icecream.entity.Users;
import com.icecream.mapper.UsersMapper;
import com.icecream.service.UsersService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service(value="usersService")
public class UsersServiceImpl implements UsersService {

    @Resource
    private UsersMapper usersMapper;

    @Override
    public List<Users> getAll() {
        return usersMapper.getAll();
    }

    @Override
    public Users loginPage(String id, String pwd) {
        return usersMapper.loginPage(id, pwd);
    }

    /**
     * 获取用户密码
     * @param username 用户名
     * @return
     */
    @Override
    public String getPassword(String username) {
        return usersMapper.getPassword(username);
    }

    /**
     * 获取用户权限
     * @param username 用户名
     * @return
     */
    @Override
    public String getRole(String username) {
        return usersMapper.getPassword(username);
    }
}
