package com.sparkle.controller;

import com.sparkle.entity.Response;
import com.sparkle.entity.User;
import com.sparkle.mapper.UserMapper;
import com.sparkle.service.UserService;
import com.sparkle.util.JWTUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author sparkle
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    private UserMapper userMapper;

    @ResponseBody
    @GetMapping("/login/token")
    public Response login(@RequestParam("phone") String phone, @RequestParam("password") String password, HttpServletResponse response) {
        Map<String, Object> userInfo = userMapper.getUserInfo(phone);
        String token = JWTUtil.sign(userInfo);
        Cookie cookie = new Cookie("token", token);
        //30 min
        cookie.setMaxAge(1800);
        cookie.setDomain("localhost");
        cookie.setPath("/");
        response.addCookie(cookie);
        return userService.login(phone, password, userInfo);
    }

    @RequestMapping("/userInfo")
    public String userInfo() {
        return "userInfo";
    }

    @ResponseBody
    @GetMapping("/getUserInfo")
    public Response getUserInfo() {
        User user = new User("phone", "username", "address");

        return Response.success(user);
    }

    @ResponseBody
    @PostMapping("/updateUserInfo")
    public Response updateUserInfo(@RequestBody User user, HttpServletRequest request) {
        System.out.println(user);
        return Response.success("");
    }

}
