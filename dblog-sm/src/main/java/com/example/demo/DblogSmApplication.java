package com.example.demo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.demo.dao") //包下所有类都是mapper
public class DblogSmApplication {

    public static void main(String[] args) {
        SpringApplication.run(DblogSmApplication.class, args);
    }

}
