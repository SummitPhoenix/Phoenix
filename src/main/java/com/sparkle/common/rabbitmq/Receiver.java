package com.sparkle.common.rabbitmq;

import com.rabbitmq.client.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Description
 * @Author: XuanXiangHui
 * @Date: 2019/12/12 下午2:49
 */
@Component
@EnableRabbit
public class Receiver {

    @Bean
    public static void receive() throws IOException, TimeoutException {
        //建立MQ连接
        ConnectionFactory connectionFactory = new ConnectionFactory();
        // 设置 RabbitMQ 的主机名
        connectionFactory.setHost("localhost");
        // 创建一个连接
        Connection connection = connectionFactory.newConnection();
        //创建通道
        Channel channel = connection.createChannel();

        //消费者队列绑定 路由
        channel.queueBind("queue1", "amq.topic", "key1");
        channel.queueBind("queue2", "amq.topic", "key2");
        //消费者监听消息
        DefaultConsumer defaultConsumer = new DefaultConsumer(channel) {
            //重写监听方法
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
                    throws IOException {
                String msg = new String(body,"UTF-8");
                System.out.println("消费者接收消息:"+msg);
            }
        };
        channel.basicConsume("queue1",true, defaultConsumer);   //绑定队列 事件监听
        channel.basicConsume("queue2",true, defaultConsumer);   //绑定队列 事件监听
    }
}
