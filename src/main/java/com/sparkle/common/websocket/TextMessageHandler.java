package com.sparkle.common.websocket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class TextMessageHandler extends TextWebSocketHandler {

	//当前在线人数
	private static AtomicInteger currentNum = new AtomicInteger();
	
    //用于存放所有建立链接的对象
    private Map<String,WebSocketSession> allClients = new HashMap<>();


    /**
     * 处理文本消息
     * session   当前发送消息的用户的链接
     * message   发送的消息是什么
     * */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        JSONObject jsonObject = JSON.parseObject(new String(message.asBytes()));
        String to = jsonObject.getString("toUser");  //找到接收者
        String toMessage = jsonObject.getString("toMessage"); //获取到发送的内容
        String  fromUser = (String) session.getAttributes().get("name");  //获取到当前发送消息的用户姓名
        String content = "收到来自"+fromUser+"的消息，内容是:"+toMessage; //拼接的字符串
        TextMessage toTextMessage = new TextMessage(content);//创建消息对象
        sendMessage(to,toTextMessage);  //一个封装的方法，进行点对点的发送数据
    }

	//发送消息的封装方法
    public void sendMessage(String toUser,TextMessage message){
        //获取到对方的链接
        WebSocketSession session = allClients.get(toUser);//获取到对方的链接
        if (session != null && session.isOpen()) {
            try {
                session.sendMessage(message);//发送消息
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 当链接建立的时候调用
     * */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String name = (String) session.getAttributes().get("name");//获取到拦截器中设置的name
        if (name != null) {
            allClients.put(name,session);//保存当前用户和链接的关系
        }
        currentNum.incrementAndGet();
    }

    /**
     * 当链接关闭的时候
     * 这里没有做相关的代码处理
     * */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
        currentNum.decrementAndGet();
    }
}