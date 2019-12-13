package com.sparkle.common.rabbitmq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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

}
