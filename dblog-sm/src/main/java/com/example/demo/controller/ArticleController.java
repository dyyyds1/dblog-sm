package com.example.demo.controller;

import com.example.demo.common.ResultAjax;
import com.example.demo.common.SessionUtils;
import com.example.demo.model.ArticleInfo;
import com.example.demo.model.Userinfo;
import com.example.demo.service.ArticleService;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/art")
public class ArticleController {

    @Autowired
    private ArticleService articleService;
    private static final int _DESC_LEN=120;//文章简介
    /**
     * 当前登录用户的文章列表
     * @return
     */
    @RequestMapping("/myList")
    public ResultAjax myList(HttpServletRequest servletRequest){
        //1.得到当前登录用户
        Userinfo userinfo= SessionUtils.getUser(servletRequest);
        if (userinfo==null){
            return ResultAjax.fail(-1,"请先登录");
        }
        //2.根据用户id查询此用户发表的所有文章
        List<ArticleInfo> list=articleService.getListById(userinfo.getId());
        //处理list->将文章正文变成简介
        if (list!=null&&list.size()>0){
            //并发处理list集合
            list.stream().parallel().forEach((art)->{
                if (art.getContent().length()>120){
                    art.setContent(art.getContent().substring(0,_DESC_LEN));
                }
            });
        }
        //3.返回给前端
        return ResultAjax.succ(list);
    }
}
