package com.icecream.entity;

import java.io.Serializable;
import java.util.Objects;

public class Users implements Serializable {
    /**
     * 用户Id
     */
    private String usersId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户密码
     */
    private String userPwd;

    public String getUsersId() {
        return usersId;
    }

    public void setUsersId(String usersId) {
        this.usersId = usersId;
    }

    public String getUserPwd() {
        return userPwd;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return Objects.hash(usersId, userPwd);
    }
}
