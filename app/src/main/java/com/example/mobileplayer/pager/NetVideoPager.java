package com.example.mobileplayer.pager;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.example.mobileplayer.base.BasePager;
import com.example.mobileplayer.utils.LogUtil;

/**
 * 网络视频页面
 */
public class NetVideoPager extends BasePager {
    private TextView textView;
    public NetVideoPager(Context context) {
        super(context);
    }

    @Override
    public View initView() {
        LogUtil.e("网络视频页面被初始化了");
        textView = new TextView(context);
        textView.setTextColor(Color.RED);
        return null;
    }

    @Override
    public void initData() {
        super.initData();
        textView.setText("网络视频页面");
        LogUtil.e("网络视频页面的数据被初始化了");
    }
}
