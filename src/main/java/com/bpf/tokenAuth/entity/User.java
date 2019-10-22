package com.bpf.tokenAuth.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class User {

    private Integer id;
    private String username;
    @JsonIgnore
    private String password;
    private String phone;
    private String address;
    private String accountId;
    public User() {};
    
    public User(Integer id, String username, String password, Integer age, String phone, String address) {
        super();
        this.id = id;
        this.username = username;
        this.password = password;
        this.phone = phone;
        this.address = address;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", password=" + password + ", phone=" + phone
				+ ", address=" + address + "]";
	}
    
}
