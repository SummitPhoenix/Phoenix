package com.sparkle.common.websocket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * WebSocket配置
 * @author sparkle
 *
 */
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer{
	
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
    	//一开始没有在放上拦截器的后面加上 setAllowedOrigins("*") 打开链接直接链接超时 403
        webSocketHandlerRegistry.addHandler(getHandler(),"/websocket/*").addInterceptors(new ChatIntercepter()).setAllowedOrigins("*");   
    }

    @Bean
    public TextMessageHandler getHandler(){
        return new TextMessageHandler();
    }
}