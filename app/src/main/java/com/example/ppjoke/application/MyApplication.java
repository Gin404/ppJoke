package com.example.ppjoke.application;

import android.app.Application;

import com.didi.drouter.api.DRouter;

/**
 * @author: zangjin
 * @date: 2022/4/6
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        DRouter.init(this);
    }
}
