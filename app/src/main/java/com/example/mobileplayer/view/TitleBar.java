package com.example.mobileplayer.view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mobileplayer.R;

public class TitleBar extends LinearLayout implements View.OnClickListener{

    public TitleBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.titlebar, this);
//        TextView tv_search = findViewById(R.id.tv_search);
//        TextView tv_game = findViewById(R.id.tv_game);
//        ImageView tv_record = findViewById(R.id.tv_record);
//        tv_game.setOnClickListener(this);
//        tv_search.setOnClickListener(this);
//        tv_record.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_search:
                break;
            case R.id.tv_game:
                break;
            case R.id.tv_record:
                break;
            default:
                break;
        }
    }
}
