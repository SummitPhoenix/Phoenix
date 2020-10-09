package com.sparkle.common.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

public class DirectProducer {
	//队列名称
    private static final String QUEUE = "helloword";

    public static void main(String[] args) throws Exception{
        Connection connection =null;
        //通道
        Channel channel = null;
        try {
            //获取连接
            connection = RabbitMQConnectionUtil.getConnection();
            //创建与Exchange的通道，每个连接可以创建多个通道，每个通道代表一个会话任务
            channel = connection.createChannel();
            /**
             * 声明队列，如果Rabbit中没有此队列将自动创建
             * param1:队列名称
             * param2:是否持久化
             * param3:队列是否独占此连接
             * param4:队列不再使用时是否自动删除此队列
             * param5:队列参数
             */
            channel.queueDeclare(QUEUE,true , false, false, null);
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
            channel.basicPublish("", QUEUE,null , message.getBytes());
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
