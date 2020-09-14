package com.example.mobileplayer.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

import io.vov.vitamio.widget.VideoView;

public class VitamioVideoView extends VideoView {
    public VitamioVideoView(Context context) {
        super(context);
    }

    public VitamioVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VitamioVideoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
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
