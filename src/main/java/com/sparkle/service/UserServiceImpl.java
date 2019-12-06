package com.sparkle.service;

import com.sparkle.entity.ResponseBean;
import com.sparkle.mapper.UserMapper;
import com.sparkle.util.EncryptUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 
 * @author sparkle
 *
 */
@Service
public class UserServiceImpl implements UserService {
	
	@Autowired(required = false)
	private UserMapper userMapper;
	
	@Override
	public ResponseBean login(String phone, String password) {
		try {
			password = EncryptUtils.encode(password);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseBean.fail(e, "加密失败");
		}
		Map<String,Object> resultMap = userMapper.getUserInfo(phone);
		String datapassword = (String) resultMap.get("password");
		if(!password.equals(datapassword)) {
			return ResponseBean.fail(null,"密码错误登录失败");
		}
		resultMap.remove("password");
		return ResponseBean.success(resultMap);
	}

}
