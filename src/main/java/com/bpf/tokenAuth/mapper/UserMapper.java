package com.bpf.tokenAuth.mapper;

import org.apache.ibatis.annotations.Select;

import com.bpf.tokenAuth.entity.User;

public interface UserMapper {

    @Select("SELECT * FROM user WHERE phone = #{phone}")
    User findByPhone(String phone);
}
