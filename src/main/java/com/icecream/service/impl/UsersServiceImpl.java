package com.icecream.service.impl;

import com.icecream.entity.Users;
import com.icecream.mapper.UsersMapper;
import com.icecream.service.UsersService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service(value="usersService")
public class UsersServiceImpl implements UsersService {

    @Resource
    private UsersMapper usersMapper;

    private static final Logger logger = LoggerFactory.getLogger(UsersServiceImpl.class);

    @Cacheable(value = "List<Users>")
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Users> getAll() {
        logger.info("获取全部用户信息");
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

    @Override
    public int checkUserBanStatus(String username) {
        return usersMapper.checkUserBanStatus(username);
    }

    @Override
    public String getRolePermission(String username) {
        return usersMapper.getRolePermission(username);
    }

    @Override
    public String getPermission(String username) {
        return usersMapper.getPermission(username);
    }
}
