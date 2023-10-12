package com.example.demo.controller;

import com.example.demo.common.AppVariable;
import com.example.demo.common.ResultAjax;
import com.example.demo.model.Userinfo;
import com.example.demo.model.vo.UserInfoVO;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    /**
     * 注册功能
     * @param userinfo
     * @return
     */
    @RequestMapping("/reg")
    public ResultAjax reg(Userinfo userinfo){
        //1.校验参数
        if (userinfo==null||!StringUtils.hasLength(userinfo.getUsername())||
                !StringUtils.hasLength(userinfo.getPassword())){
            //参数异常
            return ResultAjax.fail(-1,"非法参数");
        }
        //2.请求service添加操作
        int res=userService.reg(userinfo);
        //3.返回结果
        return ResultAjax.succ(res);
    }

    /**
     * userInfoVO 登录功能
     * @return
     */
    @RequestMapping("/login")
    public ResultAjax login(UserInfoVO userInfoVO, HttpServletRequest request){
        //1.校验参数
        if (userInfoVO==null||!StringUtils.hasLength(userInfoVO.getUsername())
                ||!StringUtils.hasLength(userInfoVO.getPassword())){
            //非法登录
            return ResultAjax.fail(-1,"参数有误");
        }
        //2.根据用户查询对象
        Userinfo userinfo=userService.getUserByName(userInfoVO.getUsername());
        System.out.println(userinfo);
        if (userinfo==null||userinfo.getId()==0){
            return ResultAjax.fail(-2,"用户名或密码错误！");
        }
        //3.使用对象中的密码和用户输入密码比较
        if (!userInfoVO.getPassword().equals(userinfo.getPassword())){
            return ResultAjax.fail(-2,"密码错误！");
        }
        //4.比较成功后，将对象存到session中
        HttpSession session=request.getSession();
        session.setAttribute(AppVariable.SESSION_USERINFO_KEY,userinfo);
        //5.将结果返回给用户
        return ResultAjax.succ(1);
    }
}
