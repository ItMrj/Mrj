package com.xhh.frameworklib.common.config;

import android.util.Log;

import com.xhh.frameworklib.common.utils.AESUtils;

/**
 * Description:
 * <p> 配置类
 * Author: Xhh
 * Time: 2017/5/22 13:44
 * Events:
 */
public class BaseConfig {

    /**
     * 客户端版本号
     */
    public static int clientVersion = 0;
    public static String TerminalCode = "android_app";
    public static String TerminalVersion = "1.0.0";
    public static String Appid = "test0";
    public static String AppSecret = "P2klF5o6IaBLiCUErKB";

    public static String SAVE_SD_FLODER = null;
    /**
     * 获取手机型号
     */
    public static String phoneMODEL = "";//TerminalSim


    //glide缓存位置
    public static String GLIDE_CACHE_PATH = "houputech_glide_cache";
    //websocket 最大重连次数
    public static final int MAX_RECONNECT_COUNT = 5;
    //websocket 地址

//    public static final String WS_URL = "wss://fx-socket-test.houputech.com/fx/"+convertStringToHex(AESUtils.
//        encrypt("AT|"+System.currentTimeMillis()+"|"+String.valueOf((int)(Math.random()*999)+100),"pe7dW2arGd9p9914"));

    public static final String WS_URL = "wss://fx-socket.houputech.com/fx/"+"android_v_1.0.0/" + AESUtils.
            encrypt("AT|"+System.currentTimeMillis()+"|"+String.valueOf((int)(Math.random()*999)+100), "pe7dW2arGd9p9914");
    //http/https 地址
    public static final String HTTP_URL = "";



}
