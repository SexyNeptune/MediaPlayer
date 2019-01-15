package com.example.mobileplayer.pager;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.example.mobileplayer.base.BasePager;
import com.example.mobileplayer.utils.LogUtil;

/**
 * 本地音乐页面
 */
public class AudioPager extends BasePager {
    private TextView textView;
    public AudioPager(Context context) {
        super(context);
    }

    @Override
    public View initView() {
        LogUtil.e("本地音乐页面被初始化了");
        textView = new TextView(context);
        textView.setTextColor(Color.RED);
        return null;
    }

    @Override
    public void initData() {
        super.initData();
        textView.setText("本地音乐页面");
        LogUtil.e("本地音乐页面的数据被初始化了");
    }
}
