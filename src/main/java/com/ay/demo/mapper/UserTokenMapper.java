package com.ay.demo.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.bpf.tokenAuth.entity.User;

@Mapper
public interface UserTokenMapper {

    @Select("SELECT * FROM t_user WHERE phone = #{phone}")
    User findByPhone(String phone);
}
