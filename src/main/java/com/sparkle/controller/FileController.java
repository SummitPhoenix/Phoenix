package com.sparkle.controller;

import com.sparkle.entity.Response;
import com.sparkle.util.FileUploadUtil;
import com.sparkle.util.PageUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.*;

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
    public Response createSpace(@RequestParam("spaceName") String spaceName) {
        File file = new File(fileLocation);
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
        File dir = new File(fileLocation + spaceName);
        if (!dir.mkdirs()) {
            return Response.fail("创建空间文件夹失败");
        }
        return Response.success("创建空间成功");
    }

    @GetMapping("/space/{type}/{space}/{page}")
    @ResponseBody
    public List<String> getFileList(@PathVariable("type") String type, @PathVariable("space") String space, @PathVariable("page") int page) {
        space = type + "/" + space;
        File file = new File(fileLocation + space);
        File[] files = file.listFiles();
        if (files == null || files.length == 0) {
            return Collections.emptyList();
        }
        List<String> list = new ArrayList<>();
        for (File f : files) {
            list.add(f.getName());
        }
        return PageUtil.subList(list, page, 10);
    }

    /**
     * 上传图片文件夹
     */
    @PostMapping("/uploadFolder")
    @ResponseBody
    public Response uploadFileFolder(@RequestParam("space") String space, HttpServletRequest request) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        List<MultipartFile> files = multipartRequest.getFiles("fileFolder");
        String spaceLocation = fileLocation + space + "/";
        return FileUploadUtil.uploadFolder(files, spaceLocation);
    }
}
