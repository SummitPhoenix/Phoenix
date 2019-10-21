package com.ay.demo.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
	
	@Select("SELECT id,username,phone,address,password FROM t_user WHERE phone=#{phone}")
	public Map<String, Object> getUserInfo(String phone);
	
	public Map<String,Object> getBalance(String phone);
	
	public int updateAccount(String phone, String balance, String updatetime);
}
