package com.icecream.service.impl;

import com.icecream.entity.Users;
import com.icecream.mapper.UsersMapper;
import com.icecream.service.UsersService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service(value="usersService")
public class UsersServiceImpl implements UsersService {

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @Resource
    private UsersMapper usersMapper;

    private static final Logger logger = LoggerFactory.getLogger(UsersServiceImpl.class);

    @Cacheable(value = "AllUsers")
    @Async
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Users> getAll() {
        logger.info("获取全部用户信息");

        List<Users> getAllUsers = (List<Users>) redisTemplate.opsForValue().get("AllUsers");

        if (null == getAllUsers) {
            synchronized (this) {
                getAllUsers = usersMapper.getAll();
            }
        }

        return getAllUsers;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Users loginPage(String id, String pwd) {
        return usersMapper.loginPage(id, pwd);
    }

    /**
     * 获取用户密码
     * @param username 用户名
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String getPassword(String username) {
        return usersMapper.getPassword(username);
    }

    /**
     * 获取用户权限
     * @param username 用户名
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String getRole(String username) {
        return usersMapper.getRole(username);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int checkUserBanStatus(String username) {
        return usersMapper.checkUserBanStatus(username);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String getRolePermission(String username) {
        return usersMapper.getRolePermission(username);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String getPermission(String username) {
        return usersMapper.getPermission(username);
    }
}
