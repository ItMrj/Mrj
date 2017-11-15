package com.xhh.frameworklib.dto;

import java.io.Serializable;

/**
 * Description:
 * 响应消息
 * Author: xhh
 * Time: 2017/7/11
 * Events:
 */
public class ResponseMessage<T> {


    private String Id;
    private String Code;
    private int Type;
    /**
     * 响应代码
     * 100000-请求、推送成功，
     * 100001-失败：不符合业务逻辑，
     * 999999-异常：系统内部异常
     */
    private String MsgCode;
    private String Message;
    private T Data;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public int getType() {
        return Type;
    }

    public void setType(int type) {
        Type = type;
    }

    public String getMsgCode() {
        return MsgCode;
    }

    public void setMsgCode(String msgCode) {
        MsgCode = msgCode;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public T getData() {
        return Data;
    }

    public void setData(T data) {
        Data = data;
    }
}
