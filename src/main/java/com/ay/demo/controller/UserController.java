package com.ay.demo.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ay.demo.entity.ResponseBean;
import com.ay.demo.entity.User;
import com.ay.demo.entity.WebSiteVisitData;
import com.ay.demo.mapper.UserMapper;
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
	@Autowired
	private UserMapper userMapper;
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
	
	@ResponseBody
	@GetMapping("/token/getVisitNum")
	public ResponseBean getVisitNum(@RequestParam("url") String url,HttpServletResponse response,HttpServletRequest request) {
		response.setHeader("Access-Control-Allow-Credentials", "true");
		response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
		if(WebSiteVisitData.getInstance().visitNum.get() == 0) {
			int visitNum = userMapper.getVisitNum(url);
			if(visitNum == 0) {
				WebSiteVisitData.getInstance().visitNum.getAndSet(1);
				return ResponseBean.success(1);
			}
		}
		WebSiteVisitData.getInstance().visitNum.incrementAndGet();
		return ResponseBean.success(WebSiteVisitData.getInstance().visitNum);
	}
	@Scheduled(cron="0 */1 * * * ?")
	public void updateVisitNum() {
		userMapper.updateVisitNum(WebSiteVisitData.getInstance().visitNum.get());
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
