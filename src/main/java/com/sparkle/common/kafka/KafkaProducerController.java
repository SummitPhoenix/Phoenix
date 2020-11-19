//package com.sparkle.common.kafka;
//
//import org.apache.log4j.Logger;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
///**
// * @author Smartisan
// */
//@RestController
//@RequestMapping("/kafka")
//public class KafkaProducerController {
//
//    private final Logger logger = Logger.getLogger(this.getClass());
//
//    @Autowired
//    private KafkaTemplate kafkaTemplate;
//
//    @RequestMapping("send")
//    public String send(String msg){
//        logger.info("生产者生产的消息："+msg);
//        kafkaTemplate.send("test_topic", msg);
//        return "success";
//    }
//
//}