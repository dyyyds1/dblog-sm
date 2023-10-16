package com.example.demo.controller;

import com.example.demo.common.ResultAjax;
import com.example.demo.common.SessionUtils;
import com.example.demo.model.ArticleInfo;
import com.example.demo.model.Userinfo;
import com.example.demo.model.vo.UserInfoVO;
import com.example.demo.service.ArticleService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

@RestController
@RequestMapping("/art")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    private static final int _DESC_LEN=120;//文章简介
    @Resource
    private ThreadPoolTaskExecutor taskExecutor;
    @Autowired
    private UserService userService;
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

    /**
     *删除文章
     */
    @RequestMapping("/del")
    public ResultAjax del(Integer aid,HttpServletRequest request){
        //1.参数校验
        if (aid==null||aid<0){
            return ResultAjax.fail(-1,"参数错误");
        }
        //2.得到当前登录对象
        Userinfo userinfo=SessionUtils.getUser(request);
        if (userinfo==null){
            return ResultAjax.fail(-1,"请先登录!");
        }
        //3.判断文章的归属人+删除操作
        int res=articleService.del(aid, userinfo.getId());
        //4.结果给前端
        return ResultAjax.succ(res);
    }

    /**
     * 添加文章
     */
    @RequestMapping("/add")
    public ResultAjax add(ArticleInfo articleInfo,HttpServletRequest request){
        //1.校验参数
        if (articleInfo==null||!StringUtils.hasLength(articleInfo.getTitle())||
                !StringUtils.hasLength(articleInfo.getContent())){
            return ResultAjax.fail(-1,"非法参数");
        }
        //2.组装数据
        Userinfo userinfo=SessionUtils.getUser(request);
        if (userinfo==null){
            return ResultAjax.fail(-1,"请先登录！");

        }
        articleInfo.setUid(userinfo.getId());
        //3.将数据入库
        int result=articleService.add(articleInfo);
        //4.将结果展示给前端
        return ResultAjax.succ(result);
    }

    /**
     * 查询自己发表的文章详情
     */
    @RequestMapping("/update_init")
    public ResultAjax updateInit(Integer aid,HttpServletRequest request){
        //1.参数校验
        if (aid==null||aid<=0){
            return ResultAjax.fail(-1,"参数错误！");
        }
        //2.得到当前用户id
        Userinfo userinfo=SessionUtils.getUser(request);
        if (userinfo==null){
            return ResultAjax.fail(-2,"请先登录！");
        }

        //3.查询文章并校验权限 where id=#{aid} and uid=#{id}
        ArticleInfo articleInfo=articleService.getArticleByIdAndUid(aid, userinfo.getId());
        //4.将结果给前端
        return ResultAjax.succ(articleInfo);
    }

    @RequestMapping("/update")
    public ResultAjax update(ArticleInfo articleInfo,HttpServletRequest request){
        //1.校验参数
        if (articleInfo==null||
        !StringUtils.hasLength(articleInfo.getTitle())||
        !StringUtils.hasLength(articleInfo.getContent())||
        articleInfo.getId()==0){
            return ResultAjax.fail(-1,"非法参数! ");
        }
        //2.获取登录用户
        Userinfo userinfo=SessionUtils.getUser(request);
        if (userinfo==null){
            return ResultAjax.fail(-2,"请先登录! ");
        }
        articleInfo.setUid(userinfo.getId());
        //3.修改文章，并校验归属人
        int result=articleService.update(articleInfo);
        //4.返回结果
        return ResultAjax.succ(result);
    }

    /**
     * 查询文章详情页
     */
    @RequestMapping("/detail")
    public ResultAjax detail(Integer aid) throws ExecutionException, InterruptedException {
        //1.参数校验
        if (aid==null||aid<=0){
            return ResultAjax.fail(-1,"非法参数");
        }
        //2.查询文章详情
        ArticleInfo articleInfo=articleService.getDetail(aid);
        if (articleInfo==null||articleInfo.getId()<=0){
            return ResultAjax.fail(-1,"非法参数");
        }
        //3.根据uid查询用户详情
        FutureTask<UserInfoVO> userTask=new FutureTask<>(()->{
            return userService.getUserById(articleInfo.getUid());
        });
        taskExecutor.submit(userTask);
        //4.根据uid查询用户发布的总文章数
        FutureTask<Integer> artCountTask=new FutureTask<>(()->{
            return articleService.getArtCountByUid(articleInfo.getUid());
        });
        taskExecutor.submit(artCountTask);
        //5.组装
        UserInfoVO userInfoVO=userTask.get();//等待任务（线程池）执行完成
        int artCount=artCountTask.get();//等待任务（线程池）执行完成
        userInfoVO.setArtCount(artCount);
        HashMap<String, Object> result=new HashMap<>();
        result.put("user",userInfoVO);
        result.put("art",articleInfo);
        //6.返回结果给前端
        return ResultAjax.succ(result);
    }
    @RequestMapping("/increment_rcount")
    public ResultAjax incrementRCount(Integer aid) {
        // 1.效验参数
        if (aid == null || aid <= 0) {
            return ResultAjax.fail(-1, "参数有误！");
        }
        // 2.更改数据库 update articleinfo set rcount=rcount+1 where aid=#{aid}
        int result = articleService.incrementRCount(aid);
        // 3.返回结果
        return ResultAjax.succ(result);
    }

    /**
     * 查询分页功能
     */
    @RequestMapping("/getlistbypage")
    public ResultAjax getListByPage(Integer pindex, Integer psize) throws ExecutionException, InterruptedException {
        // 1.参数矫正
        if (pindex == null || pindex < 1) {
            pindex = 1; // 参数矫正
        }
        if (psize == null || psize < 1) {
            psize = 2; // 参数矫正
        }
        // 2.并发进行文章列表和总页数的查询
        // 2.1 查询分页列表数据
        int finalOffset = psize * (pindex - 1); // 分页公式
        int finalPSize = psize;
        FutureTask<List<ArticleInfo>> listTask = new FutureTask<>(() -> {
            return articleService.getListByPage(finalPSize, finalOffset);
        });
        // 2.2 查找总页数
        FutureTask<Integer> sizeTask = new FutureTask<>(() -> {
            // 总条数
            int totalCount = articleService.getCount();
            double sizeTemp = (totalCount * 1.0) / (finalPSize * 1.0);
            return (int) Math.ceil(sizeTemp);
        });
        taskExecutor.submit(listTask);
        taskExecutor.submit(sizeTask);
        // 3.组装数据
        List<ArticleInfo> list = listTask.get();
        int size = sizeTask.get();
        HashMap<String, Object> map = new HashMap<>();
        map.put("list", list);
        map.put("size", size);
        // 4.将结果返回给前端
        return ResultAjax.succ(map);
    }

}
