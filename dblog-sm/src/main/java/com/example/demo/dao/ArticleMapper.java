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
    @Insert("insert into ArticleInfo(title,content,updateTime,createTime,uid,rCount,state) values (#{title},#{content},now(),now(),#{uid},0,1)")
    int add(ArticleInfo articleInfo);

    @Select("select * from ArticleInfo where id=#{aid} and uid=#{uid}")
    ArticleInfo getArticleByIdAndUid(@Param("aid")int aid,
                                     @Param("uid")int uid);
    @Update("update ArticleInfo set title=#{title},content=#{content} " +
            "where id=#{id} and uid =#{uid}")
    int update(ArticleInfo articleInfo);

    @Select("select * from ArticleInfo where id=#{aid}")
    ArticleInfo getDetailById(@Param("aid") int aid);

    @Select("select count(*) from ArticleInfo where uid=#{uid}")
    int getArtCountByUid(@Param("uid")int uid);

    @Update("update ArticleInfo set rCount=rCount+1 where id=#{aid}")
    int incrementRCount(@Param("aid") int aid);
    @Select("select * from ArticleInfo order by id desc limit #{psize} offset #{offset}")
    public List<ArticleInfo> getListByPage(@Param("psize") int psize, @Param("offset") int offset);
    @Select("select count(*) from ArticleInfo")
    int getCount();
}
