package com.sparkle.service.Impl;

import com.sparkle.entity.ResponseBean;
import com.sparkle.mapper.mapper.UserMapper;
import com.sparkle.service.UserService;
import com.sparkle.util.EncryptUtils;
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
    public ResponseBean login(String phone, String password, Map<String, Object> userInfo) {
        try {
            password = EncryptUtils.encode(password);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBean.fail(e, "加密失败");
        }
        String datapassword = (String) userInfo.get("password");
        if (!password.equals(datapassword)) {
            return ResponseBean.fail(null, "密码错误登录失败");
        }
        userInfo.remove("password");
        return ResponseBean.success(userInfo);
    }

    @Override
    public ResponseBean calaulate(double a, double b) {
        return ResponseBean.success(a * b);
    }

}
