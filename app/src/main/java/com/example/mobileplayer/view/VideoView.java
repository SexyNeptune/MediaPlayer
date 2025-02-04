package com.example.mobileplayer.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

public class VideoView extends android.widget.VideoView {

    /**
     * 在代码中创建的时候一般用这个方法
     * @param context
     */
    public VideoView(Context context) {
        this(context,null);
    }

    /**
     * 当这个类在布局文件的时候，系统通过该构造方法实例化该类
     * @param context
     * @param attrs
     */
    public VideoView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    /**
     * 当需要设置样式的时候调用该方法
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public VideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    public void setVideoSize(int width, int heigth){
        ViewGroup.LayoutParams params = getLayoutParams();
        params.height = heigth;
        params.width = width;
        setLayoutParams(params);
    }
}
