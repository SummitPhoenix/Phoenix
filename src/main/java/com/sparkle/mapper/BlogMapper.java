package com.sparkle.mapper;

import com.sparkle.entity.Blog;

public interface BlogMapper {

    /**
     * 插入博客
     */
    int insertBlog(Blog blog);

    String showBlog(String blogId);
}