package com.ay.demo.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ay.demo.entity.ResponseBean;
import com.ay.demo.service.UserService;
import com.ay.demo.util.IPUtil;

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
		IPUtil.getIpAddress(request);
		long startTime = System.currentTimeMillis();
		ResponseBean responseBean = userService.updateAccount(phone, money);
		long time = System.currentTimeMillis() - startTime;
		System.out.println(time/1000.0);
		return responseBean;
	}
}
