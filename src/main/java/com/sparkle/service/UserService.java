package com.sparkle.service;

import com.sparkle.entity.ResponseBean;

import java.util.Map;

public interface UserService {

    ResponseBean login(String phone, String password, Map<String, Object> userInfo);

}
