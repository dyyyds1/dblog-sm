package com.example.demo.dao;

import com.example.demo.model.ArticleInfo;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ArticleMapper {
    @Select("select * from ArticleInfo where uid=#{uid} order by id desc;")
    List<ArticleInfo> getListById(@Param("uid")int uid);

    @Delete("delete from ArticleInfo where id=#{aid} and uid=#{uid}")
    int del(@Param("aid") Integer aid,int uid);
    @Insert("insert into ArticleInfo(title,content,updateTime,createTime,uid) values (#{title},#{content},now(),now(),#{uid})")
    int add(ArticleInfo articleInfo);

    @Select("select * from ArticleInfo where id=#{aid} and uid=#{uid}")
    ArticleInfo getArticleByIdAndUid(@Param("aid")int aid,
                                     @Param("uid")int uid);
    @Update("update ArticleInfo set title=#{title},content=#{content} " +
            "where id=#{id} and uid =#{uid}")
    int update(ArticleInfo articleInfo);

    @Select("select * from ArticleInfo where id=#{aid}")
    ArticleInfo getDetailById(@Param("aid") int aid);

}
