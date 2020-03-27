package com.sparkle.common.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

/**
 * 生产者
 */
public class TopicProducer {
    //队列名称
    private static final String QUEUE = "helloword";
    //交换机
    private final static String EXCHANGE_NAME = "fanout_exchange";

    public static void main(String[] args) throws Exception{
        Connection connection =null;
        //通道
        Channel channel = null;
        try {
            //获取连接
            connection = RabbitMQConnectionUtil.getConnection();
            //创建与Exchange的通道，每个连接可以创建多个通道，每个通道代表一个会话任务
            channel = connection.createChannel();
            channel.exchangeDeclare(EXCHANGE_NAME, "fanout");

            //消息
            String message = "helloword!!!"+System.currentTimeMillis();
            /**
             * 消息发布方法
             * param1：Exchange的名称，如果没有指定，则使用Default Exchange
             * param2:routingKey（路由的key）,消息的路由Key，是用于Exchange（交换机）将消息转发到指定的消息队列
             * param3:消息包含的属性
             * param4：消息体
             */
            /**
             * 这里没有指定交换机，消息将发送给默认交换机，每个队列也会绑定那个默认的交换机，但是不能显
             示绑定或解除绑定
             * 默认的交换机，routingKey等于队列名称
             */
            channel.basicPublish(EXCHANGE_NAME, "",null , message.getBytes());
            System.out.println("Send Message is:'" + message + "'");

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (channel != null) {
                channel.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }

}