package com.icecream.entity;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

/**
 * @author 96495
 */
public class Users implements Serializable {
    private static final long serialVersionUID = 8797052915712615680L;
    /**
     * 用户Id
     */
    private String usersId;

    /**
     * 用户名
     */
    @NotNull(message = "用户名不能为空")
    private String username;

    /**
     * 用户密码
     */
    @NotNull(message = "密码不能为空")
    @Size(min = 1, max = 6, message = "密码必须介于1 - 6个字符之间")
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
