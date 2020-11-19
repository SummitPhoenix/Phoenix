//package com.sparkle.common.kafka;
//
//import com.sparkle.service.UserService;
//import org.apache.dubbo.config.annotation.DubboReference;
//import org.apache.kafka.clients.consumer.ConsumerRecord;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.stereotype.Component;
//
//import java.util.Map;
//
//@Component
//public class KafkaConsumer {
//
//    @DubboReference(version = "1.0.0")
//    private UserService dubboService;
//
//    @KafkaListener(topics = "test_topic")
//    public void listen(ConsumerRecord<String, Map<String, String>> record) throws Exception {
//        Map<String, String> value = record.value();
//        System.out.printf("topic = %s, offset = %d, value = %s \n", record.topic(), record.offset(), record.value());
//    }
//
//}
