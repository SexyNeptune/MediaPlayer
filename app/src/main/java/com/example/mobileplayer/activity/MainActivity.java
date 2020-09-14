package com.example.mobileplayer.activity;


import android.Manifest;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobileplayer.R;
import com.example.mobileplayer.base.BasePager;
import com.example.mobileplayer.fragment.MyFragment;
import com.example.mobileplayer.pager.AudioPager;
import com.example.mobileplayer.pager.NetAudioPager;
import com.example.mobileplayer.pager.NetVideoPager;
import com.example.mobileplayer.pager.VideoPager;
import com.example.mobileplayer.utils.PermissionHelper;
import com.example.mobileplayer.utils.PermissionInterface;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends FragmentActivity implements View.OnClickListener , PermissionInterface {

    private FrameLayout fl_main_content;

    private RadioGroup rg_bottom_tag;

    public int position=0;//RadioGroup选中的位置

    private TextView tv_search;

    private TextView tv_game;

    private ImageView tv_record;

    private PermissionHelper helper ;

    /**
     * 页面的集合
     */
    public List<BasePager> basePagers = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fl_main_content = findViewById(R.id.fl_main_content);
        rg_bottom_tag = findViewById(R.id.rg_bottom_tag);
        rg_bottom_tag.check(R.id.rb_video);
        tv_search = findViewById(R.id.tv_search);
        tv_game = findViewById(R.id.tv_game);
        tv_record = findViewById(R.id.tv_record);

        tv_game.setOnClickListener(this);
        tv_search.setOnClickListener(this);
        tv_record.setOnClickListener(this);
        helper = new PermissionHelper(this,this);

        basePagers.add(new VideoPager(this));//添加本地视频页面-0
        basePagers.add(new AudioPager(this));//添加本地音乐界面-1
        basePagers.add(new NetVideoPager(this));//添加网络视频界面-2
        basePagers.add(new NetAudioPager(this));//添加网络音乐界面-3
        setFragment();
        helper.requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,1);

        //设置RadioGroup的监听
        rg_bottom_tag.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    default:
                        position = 0;
                        break;
                    case R.id.rb_audio:
                            position = 1;
                        break;
                    case R.id.rb_net_video:
                        position = 2;
                        break;
                    case R.id.rb_net_audio:
                        position = 3;
                        break;
                }
                setFragment();
            }
        });
//        getSupportActionBar().hide(); //隐藏标题栏
    }

    /**
     * 把页面添加到fragment
     */
    private void setFragment() {
        //1.得到FragmentManager
        FragmentManager manager = getSupportFragmentManager();
        //2.开启事务
        FragmentTransaction transaction = manager.beginTransaction();
        //3.替换
        transaction.replace(R.id.fl_main_content,new MyFragment(position,basePagers));
        //4.提交事务
        transaction.commit();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_search:
                Toast.makeText(this, "搜索", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_game:
                Toast.makeText(this, "游戏", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_record:
                Toast.makeText(this, "历史记录", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        helper.requestPermissionsResult(requestCode,permissions,grantResults);
    }


    @Override
    public void requestPermissionsSuccess(int callBackCode) {
        if (callBackCode ==1){

        }
    }

    @Override
    public void requestPermissionsFail(int callBackCode) {
        if (callBackCode ==1){
            Toast.makeText(MainActivity.this,"获取权限失败",Toast.LENGTH_SHORT).show();
        }
    }

}