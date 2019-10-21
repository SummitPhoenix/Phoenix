package com.ay.demo.websocket;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.*;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.logging.Logger;

import javax.servlet.http.HttpSession;

@CrossOrigin
@Component
@RestController
// ws访问路径
@ServerEndpoint(value = "/websocket", configurator = GetHttpSessionConfigurator.class)
public class WebSocketController {

	// Logger
	private static Logger logger = Logger.getLogger(WebSocketController.class.toString());
	// 线程安全的静态变量，表示在线连接数
	private static volatile int onlineCount = 0;
	private static ObjectMapper MAPPER = new ObjectMapper();

	// 用来存放每个客户端对应的WebSocketTest对象，适用于同时与多个客户端通信
	public static CopyOnWriteArraySet<WebSocketController> webSocketSet = new CopyOnWriteArraySet<>();
	// 若要实现服务端与指定客户端通信的话，可以使用Map来存放，其中Key可以为用户标识
	public static ConcurrentHashMap<Session, Object> webSocketMap = new ConcurrentHashMap<>();

	// 与某个客户端的连接会话，通过它实现定向推送(只推送给某个用户)
	private Session session;
	private HttpSession httpSession;
	private EndpointConfig config;

	/**
	 * 连接建立成功调用的方法
	 *
	 * @param session 可选的参数。session为与某个客户端的连接会话，需要通过它来给客户端发送数据
	 */
	@OnOpen
	public void onOpen(Session session, EndpointConfig config) throws IOException, InterruptedException {
		this.session = session;
		webSocketSet.add(this); // 加入set中
		webSocketMap.put(session, this); // 加入map中
		addOnlineCount(); // 在线数加1
		System.out.println("有新连接加入！当前在线人数为" + getOnlineCount());
	}

	/**
	 * 连接关闭调用的方法
	 */
	@OnClose
	public void onClose(Session closeSession) {
		webSocketSet.remove(this); // 从set中删除
		webSocketMap.remove(closeSession); // 从map中删除
		subOnlineCount(); // 在线数减1
		System.out.println("有一连接关闭！当前在线人数为" + getOnlineCount());
	}

	/**
	 * 收到客户端消息后调用的方法
	 * 
	 * @param message 客户端发送过来的消息
	 * @param session 可选的参数
	 */
	@OnMessage
	public void onMessage(String message, Session session) {
		System.out.println("来自客户端的消息:" + message);
		// 群发消息
		for (WebSocketController item : webSocketSet) {
			try {
				item.sendMessage(message);
			} catch (IOException e) {
				e.printStackTrace();
				continue;
			}
		}
	}

	/**
	 * 发生错误时调用
	 * 
	 * @param session
	 * @param error
	 */
	@OnError
	public void onError(Session session, Throwable error) {
		System.out.println("发生错误");
		error.printStackTrace();
	}

	/**
	 * 发送消息的方法 这个方法与上面几个方法不一样。没有用注解，是根据自己需要添加的方法。
	 * 
	 * @param message
	 * @throws IOException
	 */
	public void sendMessage(String message) throws IOException {
		this.session.getBasicRemote().sendText(message);
		// this.session.getAsyncRemote().sendText(message);
	}

	public static synchronized int getOnlineCount() {
		return onlineCount;
	}

	public static synchronized void addOnlineCount() {
		WebSocketController.onlineCount++;
	}

	public static synchronized void subOnlineCount() {
		WebSocketController.onlineCount--;
	}

}
