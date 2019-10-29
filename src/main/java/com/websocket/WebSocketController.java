package com.websocket;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bpf.tokenAuth.annotation.NoneAuth;

@Controller
@RequestMapping("websocket")
public class WebSocketController {
	
	@NoneAuth
	@GetMapping("chat")
	public String websocket() {
		return "websocket";
	}
}
