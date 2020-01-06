package com.sparkle.util;

import java.io.Serializable;

/**
 * @Description
 * @Author: XuanXiangHui
 * @Date: 2019/12/27 下午2:56
 */
public class LogonInfo implements Serializable {
    private String userName;
    private String passWord;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }
}
