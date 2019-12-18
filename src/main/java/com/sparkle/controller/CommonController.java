package com.sparkle.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Description
 * @Author: XuanXiangHui
 * @Date: 2019/11/14 下午7:16
 */
@Controller
public class CommonController {

    @RequestMapping("/")
    public String root() {
        return "redirect:/index";
    }

    @GetMapping("/index")
    public String index() {
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/photoWall")
    public String photoWall() {
        return "photoWall";
    }

    @GetMapping("/navigate")
    public String navigate() {
        return "navigate";
    }
}
