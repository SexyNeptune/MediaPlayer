package com.example.mobileplayer.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.WindowManager;

import com.example.mobileplayer.R;

public class SplashActivity extends Activity {

    private Handler handler = new Handler();

    private static final String TAG = SplashActivity.class.getSimpleName();//"SplashActivity"

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);  //隐藏状态栏
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //三秒后才执行到这里
                //执行在主线程中
                startMainActivity();
                Log.e(TAG,"当前线程的名字==" + Thread.currentThread().getName());
            }
        },3000);
    }

    private void startMainActivity(){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        //关闭当前页面
        finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e(TAG, "onTOuchEvent=Action"+ event.getAction());
        startMainActivity();
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDestroy() {
        //把所有的消息和回调移除
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}
