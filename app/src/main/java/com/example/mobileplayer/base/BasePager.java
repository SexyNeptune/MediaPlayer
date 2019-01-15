package com.example.mobileplayer.base;

import android.content.Context;
import android.view.View;

import java.io.Serializable;

public abstract class BasePager {
    /**
     * 上下文
     */
    public final Context context;

    public View rootView;

    /**
     * 是否已经初始化数据
     */
    public boolean isInitData;

    public BasePager(Context context) {
        this.context = context;
        rootView = initView();
    }

    /**
     * 强制有孩子实现特定的效果
     * @return
     */
    public abstract View initView() ;

    /**
     * 强制子页面初始化数据，联网请求数据，或者绑定数据的时候需要重写该方法
     */
    public void initData(){

    }
}
