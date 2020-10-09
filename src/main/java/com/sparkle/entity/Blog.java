package com.sparkle.entity;

import lombok.Data;

@Data
public class Blog {

    private String label;

    private String title;

    private String author;

    private String createTime;

    private String content;
}
