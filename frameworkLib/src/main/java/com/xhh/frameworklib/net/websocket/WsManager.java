package com.xhh.frameworklib.net.websocket;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.google.gson.Gson;
import com.xhh.frameworklib.common.config.BaseConfig;
import com.xhh.frameworklib.common.utils.AESUtils;
import com.xhh.frameworklib.dto.RequestParams;
import com.xhh.frameworklib.dto.ResponseMessage;
import com.xhh.frameworklib.net.websocket.listener.IResponsePackage;
import com.xhh.frameworklib.net.websocket.listener.WsStatusListener;
import com.xhh.frameworklib.ui.utils.ToastUtilsBase;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

/**
 * Description:
 * webSocket 管理类
 * Author: Xhh
 * Time: 2017/5/18 11:46
 * Events:
 */
public class WsManager implements IWsManager {
    //重连相关
    private final static int RECONNECT_INTERVAL = 10 * 1000; //重连间隔时间
    private final static long RECONNECT_MAX_TIME = 120 * 1000; //最大重连时间
    private Context mContext;
    private String wsUrl;
    private WebSocket mWebSocket;
    private OkHttpClient mOkHttpClient;
    private Request mRequest;
    private int mCurrentStatus = WsStatus.DISCONNECTED;
    private boolean isNeedReconnect = true;
    private WsStatusListener wsStatusListener;
    private Lock mLock;
    private Handler wsHandler = new Handler(Looper.getMainLooper());
    private int reconnectCount = 0;   //重连次数
    //为简便获取消息数据接口
    private IResponsePackage responsePackage;
    /*
     * 检测是否断开连接
     */
    private Runnable reconnectRunnable = new Runnable() {
        @Override
        public void run() {
            if (wsStatusListener != null) wsStatusListener.onReconnect();
            buildConnect();
        }
    };

    private static WsManager wsManager; //单列模式


    public static WsManager getInstence(Context context) {
        if (wsManager == null) {
            wsManager = new WsManager.Builder(context).wsUrl("wss://fx-socket.houputech.com/fx/"
                    + "android_v_1.0.0/" + AESUtils.encrypt("AT|" + System.currentTimeMillis()
                            + "|" + String.valueOf((int) (Math.random() * 999) + 100)
                    , "pe7dW2arGd9p9914")).build();
        }
        return wsManager;

    }


    private WebSocketListener mWebSocketListener = new WebSocketListener() {

        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            mWebSocket = webSocket;
            mCurrentStatus = WsStatus.CONNECTED;
            connected();
            if (wsStatusListener != null) wsStatusListener.onOpen(response);
        }

        @Override
        public void onMessage(WebSocket webSocket, ByteString bytes) {
            if (wsStatusListener != null) wsStatusListener.onMessage(bytes);
        }

        @Override
        public void onMessage(final WebSocket webSocket, String text) {
//            Log.e("===="," ws  = onMessage text == " + text);

            if (responsePackage != null) {
                ResponseMessage message = new Gson().fromJson(text, ResponseMessage.class);
                if (message != null) {
                    if (RequestParams.TYPE_PUSH == message.getType()) { //判断消息类型
                        responsePackage.getPushMessage(message.getCode(), text);
                    } else {
                        responsePackage.getResponseMessage(message.getCode(), text);
                    }
                }
                message = null; //由于平凡调用，需要释放对象
            }
            if (wsStatusListener != null) wsStatusListener.onMessage(text);
        }

        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            if (wsStatusListener != null) wsStatusListener.onClosing(code, reason);
        }

        @Override
        public void onClosed(WebSocket webSocket, int code, String reason) {
            if (wsStatusListener != null) wsStatusListener.onClosed(code, reason);
        }

        /**
         * 连接失败
         */
        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
            tryReconnect();
            if (wsStatusListener != null) wsStatusListener.onFailure(t, response);
        }
    };

    public WsManager(Builder builder) {
        mContext = builder.mContext;
        wsUrl = builder.wsUrl;
        mOkHttpClient = builder.mOkHttpClient;
        this.mLock = new ReentrantLock();
    }

    /**
     * 连接服务器
     */
    private void initWebSocket() {
        if (mOkHttpClient == null) {
            mOkHttpClient = new OkHttpClient.Builder()
                    .retryOnConnectionFailure(true)
                    .build();
        }
        mRequest = new Request.Builder()
                .url("wss://fx-socket.houputech.com/fx/" + "android_v_1.0.0/" + AESUtils.
                        encrypt("AT|" + System.currentTimeMillis() + "|" + String.valueOf((int) (Math.random() * 999) + 100)
                                , "pe7dW2arGd9p9914"))
                .build();
        mOkHttpClient.dispatcher().cancelAll();
        try {
            mLock.lockInterruptibly();
            try {
                mOkHttpClient.newWebSocket(mRequest, mWebSocketListener);
            } finally {
                mLock.unlock();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public WebSocket getWebSocket() {
        return mWebSocket;
    }


    public void setWsStatusListener(WsStatusListener wsStatusListener) {
        this.wsStatusListener = wsStatusListener;
    }

    public void setResponsePackage(IResponsePackage responsePackage) {
        this.responsePackage = responsePackage;
    }

    @Override
    public boolean isWsConnected() {
        return mCurrentStatus == WsStatus.CONNECTED;
    }

    @Override
    public int getCurrentStatus() {
        return mCurrentStatus;
    }

    @Override
    public void startConnect() {
        isNeedReconnect = true;
        buildConnect();
    }

    @Override
    public void stopConnect() {
        isNeedReconnect = false;
        disconnect();
    }

    /**
     * 尝试重新连接
     */
    private void tryReconnect() {
        if (!isNeedReconnect) return;
        mCurrentStatus = WsStatus.RECONNECT;

        if (!isNetworkConnected(mContext)) {
            return;
        }

        long delay = reconnectCount * RECONNECT_INTERVAL;
        wsHandler.postDelayed(reconnectRunnable, delay > RECONNECT_MAX_TIME ? RECONNECT_MAX_TIME : delay);
        reconnectCount++;
    }

    private void cancelReconnect() {
        wsHandler.removeCallbacks(reconnectRunnable);
        reconnectCount = 0;
    }

    private void connected() {
        cancelReconnect();
    }

    /**
     * 断开连接
     */
    private void disconnect() {
        if (mCurrentStatus == WsStatus.DISCONNECTED) return;
        cancelReconnect();
        if (mOkHttpClient != null) mOkHttpClient.dispatcher().cancelAll();
        if (mWebSocket != null) {
            boolean isClosed = mWebSocket.close(WsStatus.CODE.NORMAL_CLOSE, WsStatus.TIP.NORMAL_CLOSE);
            //非正常关闭连接
            if (!isClosed) {
                if (wsStatusListener != null)
                    wsStatusListener.onClosed(WsStatus.CODE.ABNORMAL_CLOSE, WsStatus.TIP.ABNORMAL_CLOSE);
            }
        }
        mCurrentStatus = WsStatus.DISCONNECTED;
    }

    private void buildConnect() {
        if (mCurrentStatus == WsStatus.CONNECTED | mCurrentStatus == WsStatus.CONNECTING | !isNetworkConnected(mContext))
            return;
        mCurrentStatus = WsStatus.CONNECTING;
        initWebSocket();
    }

    //发送消息
    @Override
    public boolean sendMessage(String msg) {
        return send(msg);
    }

    @Override
    public boolean sendMessage(ByteString byteString) {
        return send(byteString);
    }

    @Override
    public boolean sendMessage(RequestParams params) {
        return send(new Gson().toJson(params));
    }

    /**
     * 发送消息
     *
     * @param msg
     * @return
     */
    private boolean send(Object msg) {
        boolean isSend = false;
        if (mWebSocket != null && mCurrentStatus == WsStatus.CONNECTED) {
            if (msg instanceof String) {
                isSend = mWebSocket.send((String) msg);
            } else if (msg instanceof ByteString) {
                isSend = mWebSocket.send((ByteString) msg);
            }
            //发送消息失败，尝试重连
            if (!isSend) {
                tryReconnect();
            }
        }
        return isSend;
    }

    //检查网络是否连接
    private boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager
                    .getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    private static final class Builder {
        private Context mContext;
        private String wsUrl;
        private OkHttpClient mOkHttpClient;

        public Builder(Context val) {
            mContext = val;
        }

        public Builder wsUrl(String val) {
            wsUrl = val;
            return this;
        }

        public Builder client(OkHttpClient val) {
            mOkHttpClient = val;
            return this;
        }

        public WsManager build() {
            return new WsManager(this);
        }
    }
}
