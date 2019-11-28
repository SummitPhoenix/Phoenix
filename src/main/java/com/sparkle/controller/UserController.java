package com.sparkle.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sparkle.entity.ResponseBean;
import com.sparkle.entity.User;
import com.sparkle.service.UserService;

/**
 * 
 * @author sparkle
 *
 */
@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@ResponseBody
	@GetMapping("/login")
	public ResponseBean login(@RequestParam("phone") String phone, @RequestParam("password") String password) {
		return userService.login(phone, password);
	}
	
	@RequestMapping("/userInfo")
	public String userInfo() {
		return "userInfo";
	}
	
	@ResponseBody
	@GetMapping("/getUserInfo")
	public ResponseBean getUserInfo() {
		User user = new User("phone","username","address");
		return ResponseBean.success(user);
	}
	
	@ResponseBody
	@PostMapping("/updateUserInfo")
	public ResponseBean updateUserInfo(@RequestBody User user, HttpServletRequest request) {
		System.out.println(user);
		return ResponseBean.success("");
	}
	
}
