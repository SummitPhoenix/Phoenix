package com.sparkle.common.websocket;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.socket.TextMessage;

@Controller
@RequestMapping("websocket")
public class WebSocketController {
	
	@Resource
	private TextMessageHandler textMessageHandler;
	
	@GetMapping("chat")
	public String websocket() {
		return "websocket";
	}
	
	@ResponseBody
	@GetMapping("send")
	public String send(@RequestParam("toUser") String toUser, @RequestParam("message") String message) {
		TextMessage textMessage = new TextMessage(message.getBytes());
		textMessageHandler.sendMessage(toUser, textMessage);
		return "send:"+message;
	}
}
