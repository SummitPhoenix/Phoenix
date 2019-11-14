package com.ay.demo.service;

import com.ay.demo.entity.ResponseBean;

public interface UserService {

	ResponseBean login(String phone, String password);

	ResponseBean updateAccount(String phone, String money);
	
}
