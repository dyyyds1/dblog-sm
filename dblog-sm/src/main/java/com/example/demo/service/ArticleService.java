package com.example.demo.service;

import com.example.demo.dao.ArticleMapper;
import com.example.demo.model.ArticleInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleService {
    @Autowired
    private ArticleMapper articleMapper;
    public List<ArticleInfo> getListById(int uid){
        return articleMapper.getListById(uid);
    }
}