package com.example.demo.common;

import com.example.demo.model.Userinfo;
import com.example.demo.model.vo.UserInfoVO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class SessionUtils {
    /**
     * 当前登录用户
     * @param request
     * @return
     */
    public static Userinfo getUser(HttpServletRequest request){
        HttpSession session=request.getSession(false);
        if (session!=null&&
        session.getAttribute(AppVariable.SESSION_USERINFO_KEY)!=null){
            //登录状态
            return (Userinfo)session.getAttribute(AppVariable.SESSION_USERINFO_KEY);
        }
        return null;
    }
}
