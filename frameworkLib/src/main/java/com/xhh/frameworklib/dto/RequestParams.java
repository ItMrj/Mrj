package com.xhh.frameworklib.dto;

/**
 * Description:
 * 请求参数
 * Author: xhh
 * Time: 2017/7/11
 * Events:
 */
public class RequestParams {

    public static final int TYPE_RESPONSE = 0; //响应
    public static final int TYPE_REQUEST = 1; //请求
    public static final int TYPE_PUSH = 2; //推送

    private String Code;
    private String Channel = "android";//渠道-默认为android
    /**
     * 消息类型， 0-响应，1-请求，2-推送
     */
    private int Type;
    private String MsgCode;
    private String Data;

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getChannel() {
        return Channel;
    }

    public void setChannel(String channel) {
        Channel = channel;
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

    public String getData() {
        return Data;
    }

    public void setData(String data) {
        Data = data;
    }
}
