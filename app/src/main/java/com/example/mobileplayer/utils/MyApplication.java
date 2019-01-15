package com.example.mobileplayer.utils;

import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    @org.jetbrains.annotations.Contract(pure = true)
    public static Context getContext(){
        return context;
    }
}
