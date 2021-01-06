package com.sparkle.controller;

import com.sparkle.entity.Response;
import com.sparkle.util.FileUploadUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Description
 * @Author: XuanXiangHui
 * @Date: 2019/11/15 下午2:15
 */
@Slf4j
@Controller
@RequestMapping("/photo")
public class PhotoController {

    @Value("${fileLocation}")
    private String fileLocation;

    @GetMapping("/photoWall")
    public String photoWall() {
        return "photoWall";
    }

    @GetMapping("/getSpaceList")
    @ResponseBody
    public List<String> getSpaceList() {
        File file = new File(fileLocation + "img");
        File[] files = file.listFiles();
        List<String> list = new ArrayList<>();
        for (File f : files) {
            list.add(f.getName());
        }
        return list;
    }

    @GetMapping("/createSpace")
    @ResponseBody
    public Response createSpace(@RequestParam("spaceName") String spaceName) {
        File file = new File(fileLocation + "img");
        File[] files = file.listFiles();
        Set<String> set = new HashSet<>();
        if (files != null) {
            for (File f : files) {
                set.add(f.getName());
            }
        }
        if (set.contains(spaceName)) {
            return Response.fail("空间名已被使用");
        }
        File dir = new File(fileLocation + "img/" + spaceName);
        dir.mkdirs();
        return Response.success("创建空间成功");
    }

    @RequestMapping("/upload")
    public String upload() {
        return "upload";
    }

    /**
     * 上传图片文件夹
     */
    @PostMapping("/uploadFolder")
    @ResponseBody
    public Response uploadFileFolder(@RequestParam("space") String space, HttpServletRequest request) {
        MultipartHttpServletRequest params = (MultipartHttpServletRequest) request;
        //fileFolder为文件项的name值
        List<MultipartFile> files = params.getFiles("fileFolder");
        String spaceLocation = fileLocation + "img/" + space + "/";
        return FileUploadUtil.uploadFolder(files, spaceLocation);
    }
}
