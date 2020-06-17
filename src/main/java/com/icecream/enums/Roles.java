package com.icecream.enums;

/**
 * @author 96495
 */
public enum Roles {
    /**
     * 管理员用户
     */
    ADMIN("admin"),

    /**
     * 普通用户
     */
    NORMAL("user");

    private String roles;

    Roles(String roles) {
        this.roles = roles;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }
}
