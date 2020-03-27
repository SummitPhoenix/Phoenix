package com.sparkle.common.websocket;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.util.Map;

/**
 * websocket握手的拦截器，检查握手请求和响应，对websockethandler传递属性，用于区别websocket
 * */
public class ChatIntercepter extends HttpSessionHandshakeInterceptor {
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        //我们为了区别链接之前是不是通过用户名来区别是谁的，此处我们还是一样的逻辑通过名字区分
        //现在我们获取到用户的名字，因为我们的地址是使用的rest风格，定义的地址是最后以为是名字，所以此处我们只需要找到请求地址拿到最后一位就行
        System.out.println("握手之前");
        String url = request.getURI().toString();
        //如果此处 url.lastIndexOf 后面没有 +1 则会带上 /
        String name = url.substring(url.lastIndexOf("/") + 1);
        //给当前链接设置名字
        attributes.put("name",name);//建议将name抽取为静态常量
        return super.beforeHandshake(request, response, wsHandler, attributes);
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, @Nullable Exception ex) {
        System.out.println("握手之后");
        super.afterHandshake(request, response, wsHandler, ex);
    }
}