package com.icecream.service.impl;

import com.icecream.entity.WeaponKind;
import com.icecream.mapper.WeaponKindMapper;
import com.icecream.service.WeaponKindService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;

@Service(value = "weaponKindService")
public class WeaponKindServiceImpl implements WeaponKindService {

    @Resource
    private WeaponKindMapper weaponKindMapper;

    @Override
    public ArrayList<WeaponKind> getAllWeapon() {
        return weaponKindMapper.getAllWeapon();
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
