package com.sparkle.common.rabbitmq;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate.ReturnCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.UUID;

/**
 * @Description
 * @Author: XuanXiangHui
 * @Date: 2019/12/12 下午2:42
 */
@Component
public class Sender implements RabbitTemplate.ConfirmCallback, ReturnCallback {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostConstruct
    public void init() {
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnCallback(this);
    }

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if (ack) {
            System.out.println("消息发送成功:" + correlationData);
        } else {
            System.out.println("消息发送失败:" + cause);
        }

    }

    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        System.out.println(message.getMessageProperties().getCorrelationId() + " 发送失败");

    }

    //发送消息，不需要实现任何接口，供外部调用。
    public void send(String msg, String routingKey) {

        CorrelationData correlationId = new CorrelationData(UUID.randomUUID().toString());

        System.out.println("开始发送消息 : " + msg);
        rabbitTemplate.convertAndSend("amq.topic", routingKey, msg, correlationId);
        System.out.println("结束发送消息 : " + msg);
    }

}
