package com.sparkle.entity;

public class User {
    private String username;
    private String phone;
    private String address;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public User() {
    }

    public User(String phone, String username, String address) {
        super();
        this.phone = phone;
        this.username = username;
        this.address = address;
    }

    @Override
    public String toString() {
        return "User [phone=" + phone + ", username=" + username + ", address=" + address + "]";
    }
}
