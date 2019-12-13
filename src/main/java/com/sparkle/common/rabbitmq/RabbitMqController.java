package com.sparkle.common.rabbitmq;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;

/**
 * @Description
 * @Author: XuanXiangHui
 * @Date: 2019/12/12 下午4:32
 */
@RequestMapping("/rabbitmq")
@Controller
public class RabbitMqController {

    @Autowired
    private Sender sender;

    @RequestMapping("/send")
    @ResponseBody
    public String send(@RequestParam("msg") String msg, @RequestParam("routingKey") String routingKey) {
        sender.send(msg, routingKey);
        return "send success";
    }

    @RequestMapping("/initReceiver")
    @ResponseBody
    public String initReceiver(){
        // 根据运行环境获取表名
        String[] queues = {"queue1","queue2","queue3"};
        // 获取注解
        RabbitListener rabbitListener = null;
        try{
            Method method = Receiver.class.getMethod("processMessage", String.class);
            rabbitListener = method.getAnnotation(RabbitListener.class);
            // 获取代理处理器
            InvocationHandler invocationHandler = Proxy.getInvocationHandler(rabbitListener);
            // 过去私有 memberValues 属性
            Field f = invocationHandler.getClass().getDeclaredField("memberValues");
            f.setAccessible(true);
            // 获取实例的属性map
            Map<String, Object> memberValues = (Map<String, Object>) f.get(invocationHandler);
            // 修改属性值
            memberValues.put("queues", queues);
        } catch (Exception e) {
            e.printStackTrace();
        }
        queues = rabbitListener.queues();
        String value = "";
        for(String s:queues){
            value += s;
        }
        return value;
    }

}
