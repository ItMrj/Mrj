package com.xhh.frameworklib.net.websocket.listener;

import okhttp3.Response;
import okio.ByteString;

/**
 * Description:
 * 可用于监听ws连接状态并进一步拓展
 * Author: Xhh
 * Time: 2017/5/18 11:46
 * Events:
 */
public abstract class WsStatusListener {
    public void onOpen(Response response) {
    }

    public void onMessage(String text) {
    }

    public void onMessage(ByteString bytes) {
    }

    public void onReconnect() {

    }

    public void onClosing(int code, String reason) {
    }


    public void onClosed(int code, String reason) {
    }

    public void onFailure(Throwable t, Response response) {
    }
}
