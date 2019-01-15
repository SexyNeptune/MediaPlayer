package com.example.mobileplayer.pager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.mobileplayer.R;
import com.example.mobileplayer.activity.SystemVideoPlayer;
import com.example.mobileplayer.adapter.VideoPagerAdapter;
import com.example.mobileplayer.base.BasePager;
import com.example.mobileplayer.domain.MediaItem;
import com.example.mobileplayer.utils.LogUtil;

import java.util.ArrayList;

/**
 * 本地视频页面
 */
public class VideoPager extends BasePager{

    private ListView listView;
    private TextView tv_nomeida;
    private ProgressBar pb_loading;

    private VideoPagerAdapter videoAdapter;

    /**
     * 装数据的集合
     */
    private ArrayList<MediaItem> mediaItems;

    public VideoPager(Context context) {
        super(context);
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(mediaItems != null && mediaItems.size() >0 ) {
                //有数据
                //设置适配器
                videoAdapter = new VideoPagerAdapter(context, mediaItems);
                listView.setAdapter(videoAdapter);
                //把文本隐藏
                tv_nomeida.setVisibility(View.GONE);
            }else{
                //没有数据
                //显示文本
                tv_nomeida.setVisibility(View.VISIBLE);

            }
            pb_loading.setVisibility(View.GONE);
        }
    };

    /**
     * 有父类调用
     */
    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.video_pager, null);
        listView = view.findViewById(R.id.video_list_view);
        tv_nomeida = view.findViewById(R.id.tv_nomeida);
        pb_loading = view.findViewById(R.id.pb_loading);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MediaItem mediaItem = mediaItems.get(i);
//                Toast.makeText(MyApplication.getContext(), mediaItem.toString(), Toast.LENGTH_SHORT).show();

//                //1.调用系统所有的播放器，隐式意图
//                Intent intent = new Intent();
//                intent.setDataAndType(Uri.parse(mediaItem.getData()), "video/*");
//                context.startActivity(intent);

                //2.调用自己写的播放器，显式意图
//                Intent intent = new Intent(context, SystemVideoPlayer.class);
//                intent.setDataAndType(Uri.parse(mediaItem.getData()), "video/*");
//                context.startActivity(intent);

                //3.传递列表数据- 序列化 - 对象
                Intent intent = new Intent(context, SystemVideoPlayer.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("videolist",mediaItems);
                intent.putExtras(bundle);
                intent.putExtra("position" , i);
                context.startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        LogUtil.e("本地视频初始化了");
        //加载本地视频
        getDataFromLocal();
    }

    /**
     * 从本地的sdcard中得到数据
     * 1.遍历，后缀名
     * 2.从内容提供器中获取视频
     */
    private void getDataFromLocal() {

        new Thread(){
            @Override
            public void run() {
                super.run();
                mediaItems = new ArrayList<>();
                ContentResolver resolver = context.getContentResolver();
                Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                String strs [] = {
                        MediaStore.Video.Media.DISPLAY_NAME, //视频文件在sdcard中的名称
                        MediaStore.Video.Media.DURATION, //视频文件的时长
                        MediaStore.Video.Media.SIZE, //视频文件的大小
                        MediaStore.Video.Media.DATA, //视频文件的绝对地址
                        MediaStore.Video.Media.ARTIST, //歌曲的演唱者
                };
                Cursor cursor = resolver.query(uri,strs,null,null,null);
                if (cursor != null) {
                    while(cursor.moveToNext()){

                        MediaItem mediaItem = new MediaItem();

                        mediaItems.add(mediaItem);//add()操作放在前面可行，里面mediaIem的值在后面改变此处也会改变；

                        String name = cursor.getString(0);
                        mediaItem.setName(name);

                        long duration = cursor.getLong(1);
                        mediaItem.setDuration(duration);

                        long size = cursor.getLong(2);
                        mediaItem.setSize(size);

                        String data = cursor.getString(3);
                        mediaItem.setData(data);

                        String artist = cursor.getString(4);
                        mediaItem.setArtist(artist);

                    }
                    cursor.close();
                }

                //handler发消息
                handler.sendEmptyMessage(10);

            }
        }.start();
    }

    /**
     * 解决安卓6.0以上版本不能读取外部存储权限的问题
     * @param activity
     * @return
     */
    public static boolean isGrantExternalRW(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && activity.checkSelfPermission(
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            activity.requestPermissions(new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, 1);

            return false;
        }

        return true;
    }

}
