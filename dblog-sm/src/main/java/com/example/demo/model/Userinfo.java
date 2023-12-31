package com.example.demo.model;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class Userinfo implements Serializable {
    private int id;
    private String username;
    private String password;
    private String photo;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private int state;
}
