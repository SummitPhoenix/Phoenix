package com.ay.demo.service;

import com.ay.demo.entity.ResponseBean;

public interface UserService {
	public ResponseBean login(String phone, String password);
	public ResponseBean updateAccount(String phone, String money);
}
