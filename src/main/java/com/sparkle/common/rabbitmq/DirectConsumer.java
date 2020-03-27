package com.sparkle.common.rabbitmq;

import java.io.IOException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class DirectConsumer {
	//队列名称
    private static final String QUEUE= "helloword";

    public static void main(String[] args) throws Exception{
        Connection connection = null;
        Channel channel = null;

        try {
            //获取连接
            connection = RabbitMQConnectionUtil.getConnection();
            //创建通道
            channel = connection.createChannel();
            //声明队列(队列名称，是否持久化,是否独占此连接,不使用时是否自动删除此队列,队列参数)
            channel.queueDeclare(QUEUE, true, false, false, null);
            //定义消费方法
            Channel finalChannel = channel;
            //匿名内部类
            DefaultConsumer consumer = new DefaultConsumer(finalChannel){
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    //交换机
                    String exchange = envelope.getExchange();
                    //路由key
                    String routingKey = envelope.getRoutingKey();
                    //消息id
                    long deliveryTag = envelope.getDeliveryTag();
                    //消息内容
                    String msg = new String(body,"utf8");
                    System.out.println("receive message.." + msg);
                    //如果正常处理后需要做回复
                    finalChannel.basicAck(deliveryTag,false );
                }
            };
            /**
             * 监听队列:QUEUE 如果有消息来了，通过consumer来处理
             * 参数明细
             * 1、队列名称
             * 2、是否自动回复，设置为true为表示消息接收到自动向mq回复接收到了，mq接收到回复会删除消息，设置
             为false则需要手动回复
             * 3、消费消息的方法，消费者接收到消息后调用此方法
             */
            channel.basicConsume(QUEUE,false ,consumer );
            //阻塞住，让他一直监听
            System.in.read();
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
