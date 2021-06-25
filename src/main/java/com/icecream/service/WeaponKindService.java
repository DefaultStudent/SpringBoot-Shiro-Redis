package com.icecream.service;

import com.icecream.entity.WeaponKind;

import java.util.ArrayList;

public interface WeaponKindService {

    /**
     *
     * @return
     */
    ArrayList<WeaponKind> getAllWeapon();

    /**
     *
     * @param id
     * @param Name
     * @return
     */
    boolean insertWeaponKind(int id, String Name);
}
