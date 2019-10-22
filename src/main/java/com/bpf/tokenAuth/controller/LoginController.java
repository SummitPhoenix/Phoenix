package com.bpf.tokenAuth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ay.demo.mapper.UserTokenMapper;

@Controller
@RequestMapping("/token")
public class LoginController {

    @Autowired
    private UserTokenMapper userTokenMapper;
    
    @ResponseBody
    @GetMapping("user/{phone}")
    public Object getUserByPhone(@PathVariable String phone) {
        return userTokenMapper.findByPhone(phone);
    }
    @RequestMapping("/login")
    public String test() {
    	return "login";
    }
    @RequestMapping("/index")
    public String index() {
    	return "index";
    }
    
}
