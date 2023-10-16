package com.example.demo.dao;

import com.example.demo.model.Userinfo;
import com.example.demo.model.vo.UserInfoVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;


@Mapper
public interface UserMapper {
    @Insert("insert into userinfo(username,password) values (#{username},#{password})")
    int reg(Userinfo userinfo);
    @Select("select * from userinfo where username= #{username}")
    Userinfo getUserByName(@Param("username")String username);

    @Select("select * from userinfo where id = #{uid}")
    UserInfoVO getUserById(@Param("uid") int uid);
}
