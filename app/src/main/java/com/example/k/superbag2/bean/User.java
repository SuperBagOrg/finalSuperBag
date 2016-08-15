package com.example.k.superbag2.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by Aersasi on 2016/8/14.
 */
public class User extends BmobObject {
    String name;
    String password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
