package com.xhh.frameworklib.framebase;

import android.support.multidex.MultiDexApplication;

/**
 * Description:
 * Application
 * Author: Xhh
 * Time: 2017/5/18 13:47
 * Events:
 */
public class FrameBaseApplication extends MultiDexApplication {


    private static FrameBaseApplication application;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;

    }
    public static FrameBaseApplication getInstance() {
        return application;
    }
}
