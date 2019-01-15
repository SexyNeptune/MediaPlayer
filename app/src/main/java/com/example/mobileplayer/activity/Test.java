package com.example.mobileplayer.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.mobileplayer.utils.LogUtil;

public class Test extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        LogUtil.e("onCreate-------");
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        LogUtil.e("onStart------");
        super.onStart();
    }

    @Override
    protected void onRestart() {
        LogUtil.e("onRestart------");
        super.onRestart();
    }

    @Override
    protected void onResume() {
        LogUtil.e("onResume------");
        super.onResume();
    }

    @Override
    protected void onPause() {
        LogUtil.e("onPause------");
        super.onPause();
    }

    @Override
    protected void onStop() {
        LogUtil.e("onStop------");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        LogUtil.e("onDestroy------");
        super.onDestroy();
    }
}
