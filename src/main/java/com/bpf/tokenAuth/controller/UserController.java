package com.bpf.tokenAuth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.bpf.tokenAuth.entity.User;
import com.bpf.tokenAuth.mapper.UserMapper;

@RestController
public class UserController {

    @Autowired
    private UserMapper userMapper;
    
    @GetMapping(value="/user/{phone}")
    public Object getUserByPhone(@PathVariable String phone) {
        User user = userMapper.findByPhone(phone);
        return user;    
    }
}
