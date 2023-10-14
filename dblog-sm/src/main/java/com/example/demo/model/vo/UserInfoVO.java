package com.example.demo.model.vo;

import com.example.demo.model.Userinfo;
import lombok.Data;

@Data
public class UserInfoVO extends Userinfo {
    private String checkCode;
    private int artCount;
}
