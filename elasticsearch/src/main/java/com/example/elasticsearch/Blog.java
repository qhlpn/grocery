package com.example.elasticsearch;

import lombok.Data;

import java.util.Date;

@Data
public class Blog {

    private int id;
    private String author;
    private String influence;
    private String title;
    private String content;
    private String tag;
    private int vote;
    private int view;
    private Date createAt;


}
