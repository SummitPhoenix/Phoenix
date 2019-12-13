package com.sparkle.mapper.mapper;

import com.sparkle.entity.Blog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BlogMapper {

	/**
	 * 插入博客
	 */
	int insertBlog(Blog blog);

	String showBlog(String blogId);
}