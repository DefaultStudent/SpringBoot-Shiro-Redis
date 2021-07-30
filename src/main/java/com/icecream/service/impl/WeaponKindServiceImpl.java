package com.icecream.service.impl;

import com.icecream.entity.WeaponKind;
import com.icecream.mapper.WeaponKindMapper;
import com.icecream.service.WeaponKindService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;

@Service(value = "weaponKindService")
public class WeaponKindServiceImpl implements WeaponKindService {

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @Resource
    private WeaponKindMapper weaponKindMapper;

    private static final Logger LOGGER = LoggerFactory.getLogger(WeaponKindServiceImpl.class);

    @Override
    @Cacheable(value = "AllWeapons")
    @Async
    @Transactional(rollbackFor = Exception.class)
    public ArrayList<WeaponKind> getAllWeapon() {

        LOGGER.info("获取全部武器信息");
        
        ArrayList<WeaponKind> getAllWeaponKinds = (ArrayList<WeaponKind>) redisTemplate.opsForValue().get("AllWeapons");

        if (null == getAllWeaponKinds) {
            synchronized (this) {
                getAllWeaponKinds = weaponKindMapper.getAllWeapon();
            }
        }

        return getAllWeaponKinds;
    }

    @Override
    public boolean insertWeaponKind(int id, String Name) {

        WeaponKind weaponKind = new WeaponKind();

        weaponKind.setWeaponKindId(id);
        weaponKind.setWeaponKindName(Name);

        if (weaponKindMapper.insertOne(weaponKind)){
            return true;
        } else {
            return false;
        }
    }
}
