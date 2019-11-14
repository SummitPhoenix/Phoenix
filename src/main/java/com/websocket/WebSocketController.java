package com.websocket;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("websocket")
public class WebSocketController {
	
	@GetMapping("chat")
	public String websocket() {
		return "websocket";
	}
}
