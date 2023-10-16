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

    public int del(Integer aid,int uid){
        return articleMapper.del(aid,uid);
    }
    public int add(ArticleInfo articleInfo){
        return articleMapper.add(articleInfo);
    }

    public ArticleInfo getArticleByIdAndUid(int aid,int uid){
        return articleMapper.getArticleByIdAndUid(aid,uid);
    }
    public int update(ArticleInfo articleInfo){
        return articleMapper.update(articleInfo);
    }
    public ArticleInfo getDetail(int aid){
        return articleMapper.getDetailById(aid);
    }

    public int getArtCountByUid(int uid){
        return articleMapper.getArtCountByUid(uid);
    }
    public int incrementRCount(int aid) {
        return articleMapper.incrementRCount(aid);
    }

    public List<ArticleInfo> getListByPage(int psize, int offset) {
        return articleMapper.getListByPage(psize, offset);
    }
    public int getCount(){
        return articleMapper.getCount();
    }
}
