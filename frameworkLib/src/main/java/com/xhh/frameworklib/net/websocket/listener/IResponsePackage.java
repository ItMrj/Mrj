package com.xhh.frameworklib.net.websocket.listener;

import com.xhh.frameworklib.dto.ResponseMessage;

import okio.ByteString;

/**
 * Description:
 * 接受消息
 * Author: Xhh
 * Time: 2017/5/19 16:42
 * Events:
 */
public interface IResponsePackage {
    //响应消息
    void getResponseMessage(String code, String message);

    //推送消息
    void getPushMessage(String code, String message);
}
