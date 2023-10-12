package com.example.demo.dao;

import com.example.demo.model.ArticleInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ArticleMapper {
    @Select("select * from ArticleInfo where uid=#{uid} ;")
    List<ArticleInfo> getListById(@Param("uid")int uid);
}
