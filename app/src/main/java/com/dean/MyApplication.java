package com.dean;

import android.app.Application;

/**
 * Created by chw on 2016/8/8.
 */

public class MyApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        //Intent intent = new Intent(this, MyService.class);
        //startService(intent);
    }
}
