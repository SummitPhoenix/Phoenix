package com.sparkle.controller;

import com.sparkle.entity.ResponseBean;
import com.sparkle.util.FileUploadUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
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
@Controller
@RequestMapping("/photo")
public class PhotoController {

    @Value("${photoLocation}")
    private String photoLocation;

    @GetMapping("/photoWall")
    public String photoWall() {
        return "photoWall";
    }

    @GetMapping("/getSpaceList")
    @ResponseBody
    public List<String> getSpaceList() {
        File file = new File(photoLocation);
        File[] files = file.listFiles();
        List<String> list = new ArrayList<>();
        for (File f : files) {
            list.add(f.getName());
        }
        return list;
    }

    @GetMapping("/createSpace")
    @ResponseBody
    public ResponseBean createSpace(@RequestParam("spaceName") String spaceName) {
        File file = new File(photoLocation);
        File[] files = file.listFiles();
        Set<String> set = new HashSet<>();
        for (File f : files) {
            set.add(f.getName());
        }
        if (set.contains(spaceName)) {
            return ResponseBean.fail("空间名已被使用");
        }
        File dir = new File(photoLocation + "/" + spaceName);
        dir.mkdirs();
        return ResponseBean.success("创建空间成功");
    }

    @Cacheable(cacheNames = {"photolist"})
    @GetMapping("/getPhotoList")
    @ResponseBody
    public List<String> getPhotoList(@RequestParam("space") String space, @RequestParam("page") int page) {
        File file = new File(photoLocation + "/" + space);
        File[] files = file.listFiles();
        List<String> list = new ArrayList<>();
        for (File f : files) {
            list.add(f.getName());
            System.out.println(f.getName());
        }
        return list.subList(page, page + 10);
    }

    @RequestMapping("/upload")
    public String upload() {
        return "upload";
    }

    /**
     * 上传图片文件夹
     */
    @CachePut(value = "photolist", key = "#result.space")
    @PostMapping("/uploadFolder")
    @ResponseBody
    public ResponseBean uploadFileFolder(@RequestParam("space") String space, HttpServletRequest request) {
        MultipartHttpServletRequest params = (MultipartHttpServletRequest) request;
        //fileFolder为文件项的name值
        List<MultipartFile> files = params.getFiles("fileFolder");
        String spaceLocation = photoLocation + "/" + space + "/";
        return FileUploadUtil.upload(files, spaceLocation);
    }
}
