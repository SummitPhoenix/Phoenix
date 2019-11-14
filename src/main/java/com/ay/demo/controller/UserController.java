package com.ay.demo.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ay.demo.entity.ResponseBean;
import com.ay.demo.entity.User;
import com.ay.demo.service.UserService;

/**
 * 
 * @author sparkle
 *
 */
@Controller
public class UserController {
	
	@Autowired
	private UserService userService;

	@GetMapping("/getindex")
	public String index() {
		return "index";
	}
	
	@ResponseBody
	@GetMapping("/login")
	public ResponseBean login(@RequestParam("phone") String phone, @RequestParam("password") String password) {
		return userService.login(phone, password);
	}
	
	@ResponseBody
	@GetMapping("/updateAccount")
	public ResponseBean updateAccount(@RequestParam("phone") String phone, @RequestParam("money") String money, HttpServletRequest request) {
		long startTime = System.currentTimeMillis();
		ResponseBean responseBean = userService.updateAccount(phone, money);
		long time = System.currentTimeMillis() - startTime;
		System.out.println(time/1000.0);
		return responseBean;
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
