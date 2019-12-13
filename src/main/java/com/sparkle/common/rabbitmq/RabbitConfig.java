//package com.sparkle.common.rabbitmq;
//
//import com.rabbitmq.client.*;
//import org.springframework.amqp.core.Binding;
//import org.springframework.amqp.core.BindingBuilder;
//import org.springframework.amqp.core.Queue;
//import org.springframework.amqp.core.TopicExchange;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.io.IOException;
//import java.util.concurrent.TimeoutException;
//
///**
// * @Description
// * @Author: XuanXiangHui
// * @Date: 2019/12/12 下午2:40
// */
//@Configuration
//public class RabbitConfig {
//
//    public static TopicExchange topicExchange = new TopicExchange("amq.topic");
//
//    //声明队列
//    @Bean
//    public Queue queue1() {
//        // true表示持久化该队列
//        return new Queue("queue1", true);
//    }
//
//    @Bean
//    public Queue queue2() {
//        return new Queue("queue2", true);
//    }
//
//    //声明交互器
//    @Bean
//    TopicExchange topicExchange() {
//        return new TopicExchange("amq.topic");
//    }
//
//    //绑定
//    @Bean
//    public Binding binding1() {
//        return BindingBuilder.bind(queue1()).to(topicExchange()).with("key.1");
//    }
//
//    @Bean
//    public Binding binding2() {
//        return BindingBuilder.bind(queue2()).to(topicExchange()).with("key.2");
//    }
//
//    public static Queue createUserQueue(String userId){
//        return new Queue(userId,true);
//    }
//
//    public static Binding createBinding(Queue userQueue, String producerRoutingKey){
//        return BindingBuilder.bind(userQueue).to(topicExchange).with(producerRoutingKey);
//    }
//
//    public void test() throws IOException, TimeoutException {
//        //建立MQ连接
//        ConnectionFactory connectionFactory = new ConnectionFactory();
//        // 设置 RabbitMQ 的主机名
//        connectionFactory.setHost("localhost");
//        // 创建一个连接
//        Connection connection = connectionFactory.newConnection();
//        //创建通道
//        Channel channel = connection.createChannel();
//
//        //消费者声明队列
//        channel.queueDeclare("queue1", true, true, true, null);
//        //消费者队列绑定 路由
//        channel.queueBind("queue1", "amq.topic", "key1");
//        channel.queueBind("queue2", "amq.topic", "key2");
//        //消费者监听消息
//        DefaultConsumer defaultConsumer = new DefaultConsumer(channel) {
//            //重写监听方法
//            @Override
//            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
//                    throws IOException {
//                String msg = new String(body,"UTF-8");
//                System.out.println("消费者接收消息:"+msg);
//            }
//        };
//        channel.basicConsume("queue1",true, defaultConsumer);   //绑定队列 事件监听
//        channel.basicConsume("queue2",true, defaultConsumer);   //绑定队列 事件监听
//    }
//}
