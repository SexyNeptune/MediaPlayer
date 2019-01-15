package com.example.mobileplayer.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mobileplayer.activity.MainActivity;
import com.example.mobileplayer.base.BasePager;

import java.util.ArrayList;
import java.util.List;


@SuppressLint("ValidFragment")
public class MyFragment extends Fragment {

    private int position;

    private List<BasePager> basePagers;

    @SuppressLint("ValidFragment")
    public MyFragment(int position, List<BasePager> basePagers){
        this.basePagers = basePagers;
        this.position = position;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        BasePager basePager = getBasePager();
        if(basePager!= null){
            return basePager.rootView;
        }
        return null;
    }

    /**
     * 根据位置得到对应的页面
     * @return
     */
    private BasePager getBasePager() {
        BasePager basePager = basePagers.get(position);
        if(basePager != null&&!basePager.isInitData){

            //动态申请权限
//            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                    != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
//            }else{
                basePager.initData();//联网请求或者绑定数据
                basePager.isInitData=true;
//            }
        }
        return basePager;
    }
}
