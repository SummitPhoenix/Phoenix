package com.sparkle.service;

import com.sparkle.entity.Response;

import java.util.Map;

public interface UserService {

    Response login(String phone, String password, Map<String, Object> userInfo);

    Response calaulate(double a, double b);
}
