package com.sparkle.controller;

import com.sparkle.entity.Blog;
import com.sparkle.mapper.mapper.BlogMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @Description
 * @Author: XuanXiangHui
 * @Date: 2019/11/15 上午10:18
 */
@Controller
@RequestMapping("blog")
public class BlogController {

    @Resource
    private BlogMapper blogMapper;

    /**
     * 添加博客
     */
    @RequestMapping("/addBlog")
    public String addBlog() {
        return "addBlog";
    }

    @PostMapping("/insertBlog")
    public String insertBlog(@RequestBody Blog po) {
        blogMapper.insertBlog(po);
        return "addBlog";
    }

    @GetMapping("/blog")
    public String blog(@RequestParam("blogId") String blogId, HttpServletRequest request) {
        request.setAttribute("content", blogMapper.showBlog(blogId));
        return "blog";
    }

    @GetMapping("/showBlog")
    @ResponseBody
    public String showBlog(@RequestParam("blogId") String blogId) {
        return blogMapper.showBlog(blogId);
    }
}
