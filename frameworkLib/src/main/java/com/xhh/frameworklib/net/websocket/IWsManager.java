package com.xhh.frameworklib.net.websocket;

import com.xhh.frameworklib.dto.RequestParams;

import okhttp3.WebSocket;
import okio.ByteString;

/**
 * Description:
 * webSocket管理接口
 * Author: Xhh
 * Time: 2017/5/18 11:46
 * Events:
 */
interface IWsManager {
    WebSocket getWebSocket();

    void startConnect();

    void stopConnect();

    boolean isWsConnected();

    int getCurrentStatus();

    boolean sendMessage(String msg);

    boolean sendMessage(ByteString byteString);

    boolean sendMessage(RequestParams params);
}
