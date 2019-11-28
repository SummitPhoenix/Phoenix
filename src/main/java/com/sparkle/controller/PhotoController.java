package com.sparkle.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description
 * @Author: XuanXiangHui
 * @Date: 2019/11/15 下午2:15
 */
@Controller
@RequestMapping("/photo")
public class PhotoController {

    @Value("${photoLocation}")
    private String photoLocation;

    @RequestMapping("/photoWall")
    public String photoWall() {
        return "photoWall";
    }

    @GetMapping("/getPhotoList")
    @ResponseBody
    public List<String> getPhotoList() {
        File file = new File(photoLocation);
        File[] files = file.listFiles();
        List<String> list = new ArrayList<>();
        for(File f:files) {
            list.add(f.getName());
        }
        return list;
    }
}
