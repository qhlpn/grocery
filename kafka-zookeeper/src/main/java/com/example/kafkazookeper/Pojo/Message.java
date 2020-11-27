package com.example.kafkazookeper.Pojo;


import lombok.Builder;
import lombok.Data;

/**
 * @author QiuHongLong
 */
@Data
@Builder
public class Message {

    private Long id;
    private String msg;


}
