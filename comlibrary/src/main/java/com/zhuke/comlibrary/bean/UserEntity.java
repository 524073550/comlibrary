package com.zhuke.comlibrary.bean;

import java.io.Serializable;

/**
 * Created by 15653 on 2018/1/3.
 */


public class UserEntity implements Serializable {
    public String mobilePhone, token, userId;

    public UserEntity() {

    }

    public UserEntity(String phone, String token, String userId) {
        this.mobilePhone = phone;
        this.token = token;
        this.userId = userId;
    }

    public String getPhone() {
        return mobilePhone;
    }

    public void setPhone(String phone) {
        this.mobilePhone = phone;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "mobilePhone='" + mobilePhone + '\'' +
                ", token='" + token + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}
