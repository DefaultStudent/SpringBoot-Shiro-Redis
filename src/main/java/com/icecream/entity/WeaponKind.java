package com.icecream.entity;

import java.io.Serializable;

/**
 * @date
 * @author 96495
 */
public class WeaponKind implements Serializable {

    private static final long serialVersionUID = 1024052915712615680L;

    private int weaponKindId;

    private String weaponKindName;

    public int getWeaponKindId() {
        return weaponKindId;
    }

    public void setWeaponKindId(int weaponKindId) {
        this.weaponKindId = weaponKindId;
    }

    public String getWeaponKindName() {
        return weaponKindName;
    }

    public void setWeaponKindName(String weaponKindName) {
        this.weaponKindName = weaponKindName;
    }
}
