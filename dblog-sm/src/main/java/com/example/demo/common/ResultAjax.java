package com.example.demo.common;

import lombok.Data;

@Data
public class ResultAjax {
    private int code;
    private String msg;
    private Object data;

    public static ResultAjax succ(Object data){
        ResultAjax resultAjax=new ResultAjax();
        resultAjax.setCode(200);
        resultAjax.setMsg("");
        resultAjax.setData(data);
        return  resultAjax;
    }

    public static ResultAjax succ(int code,String msg,Object data){
        ResultAjax resultAjax=new ResultAjax();
        resultAjax.setCode(200);
        resultAjax.setMsg(msg);
        resultAjax.setData(data);
        return  resultAjax;
    }

    public static ResultAjax fail(int code,String msg){
        ResultAjax resultAjax=new ResultAjax();
        resultAjax.setCode(code);
        resultAjax.setMsg(msg);
        resultAjax.setData(null);
        return  resultAjax;
    }
}
