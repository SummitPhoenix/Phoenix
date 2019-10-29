package com.ay.demo.service;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ay.demo.entity.ResponseBean;
import com.ay.demo.mapper.UserMapper;
import com.ay.demo.util.EncryptUtils;

/**
 * 
 * @author sparkle
 *
 */
@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
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
		if(password.equals(datapassword)) {
			resultMap.remove("password");
			return ResponseBean.success(resultMap);
		}
		return ResponseBean.fail(null,"密码错误登录失败");
	}

	@Override
	@Transactional(rollbackFor = RuntimeException.class)
	public ResponseBean updateAccount(String phone, String money) {
		try {
			for(int i=0;i<3;i++) {
				Map<String,Object> resultMap = userMapper.getBalance(phone);
				String balanceValue = resultMap.get("balance").toString();
				String updatetime = resultMap.get("updatetime").toString().substring(0,19);
				BigDecimal balance = new BigDecimal(balanceValue);
				BigDecimal transaction = new BigDecimal(money);
				balance = balance.add(transaction);
				if(balance.compareTo(BigDecimal.ZERO)<1) {
					return ResponseBean.fail("","余额不足");
				}
				int result = userMapper.updateAccount(phone,balance.toString(),updatetime);
				System.out.println(Thread.currentThread().getName());
				if(result!=1) {
					Thread.sleep(10);
					if(i!=2) {
						continue;
					}
					return ResponseBean.fail("","更新失败");
				} else {
					return ResponseBean.success("");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseBean.fail("","更新失败");
	}

}
