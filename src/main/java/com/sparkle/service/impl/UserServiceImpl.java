package com.sparkle.service.impl;

import com.sparkle.entity.Response;
import com.sparkle.mapper.UserMapper;
import com.sparkle.service.UserService;
import com.sparkle.util.Aes;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author sparkle
 */
//@DubboService(version = "1.0.0")
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public Response login(String phone, String password, Map<String, Object> userInfo) {
        try {
            password = Aes.encrypt(password);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.fail(e, "加密失败");
        }
        String datapassword = (String) userInfo.get("password");
        if (!password.equals(datapassword)) {
            return Response.fail(null, "密码错误登录失败");
        }
        userInfo.remove("password");
        return Response.success(userInfo);
    }

    @Override
    public Response calaulate(double a, double b) {
        return Response.success(a * b);
    }

}
