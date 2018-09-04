package com.github2136.android_utils;

import android.app.Application;

import com.github2136.util.CrashHandler;

/**
 * Created by yb on 2018/9/4.
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        CrashHandler crashHandler=CrashHandler.getInstance();
        crashHandler.setCustomCrashHanler(this);
        crashHandler.setCallback(new CrashHandler.CrashHandlerCallback() {
            @Override
            public void finishAll() {

            }

            @Override
            public void submitLog() {

            }
        });
    }
}
