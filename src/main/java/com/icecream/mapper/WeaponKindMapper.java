package com.icecream.mapper;

import com.icecream.entity.WeaponKind;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Mapper
@Repository
public interface WeaponKindMapper {
    /**
     *
     * @return
     */
    ArrayList<WeaponKind> getAllWeapon();

    /**
     *
     * @param weaponKind
     * @return
     */
    boolean insertOne(WeaponKind weaponKind);
}
