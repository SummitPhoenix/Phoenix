package com.sparkle.controller;

import com.sparkle.entity.ResponseBean;
import com.sparkle.util.FileUploadUtil;
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
 * @author Smartisan
 */
@Controller
@RequestMapping("/file")
public class FileController {

    @Value("${fileLocation}")
    private String fileLocation;

    @GetMapping("/fileUpload")
    public String fileUpload() {
        return "fileUpload";
    }

    @GetMapping("/getSpaceList")
    @ResponseBody
    public List<String> getSpaceList() {
        File file = new File(fileLocation);
        File[] files = file.listFiles();
        List<String> list = new ArrayList<>();
        if (files != null) {
            for (File f : files) {
                list.add(f.getName());
            }
        }
        return list;
    }

    @GetMapping("/createSpace")
    @ResponseBody
    public ResponseBean createSpace(@RequestParam("spaceName") String spaceName) {
        File file = new File(fileLocation);
        File[] files = file.listFiles();
        Set<String> set = new HashSet<>();
        if (files != null) {
            for (File f : files) {
                set.add(f.getName());
            }
        }
        if (set.contains(spaceName)) {
            return ResponseBean.fail("空间名已被使用");
        }
        File dir = new File(fileLocation + spaceName);
        dir.mkdirs();
        return ResponseBean.success("创建空间成功");
    }

    /**
     * 上传图片文件夹
     */
    @PostMapping("/uploadFolder")
    @ResponseBody
    public ResponseBean uploadFileFolder(@RequestParam("space") String space, HttpServletRequest request) {
        MultipartHttpServletRequest params = (MultipartHttpServletRequest) request;
        //fileFolder为文件夹的name
        List<MultipartFile> files = params.getFiles("fileFolder");
        String spaceLocation = fileLocation + space + "/";
        return FileUploadUtil.upload(files, spaceLocation);
    }
}
