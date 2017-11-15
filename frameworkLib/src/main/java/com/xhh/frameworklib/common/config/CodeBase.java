package com.xhh.frameworklib.common.config;

/**
 * @author xhh
 */
public interface CodeBase {

    interface System {
        int BASE = 0xFF000000;
        int Immersive = BASE + 1;//启用沉浸式状态栏
        int DefaultColor = BASE + 2;//启用默认颜色
        int DefaultLight = BASE + 3;//启用白色
    }


}
