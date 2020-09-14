package com.example.mobileplayer.pager;

import android.content.Context;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.mobileplayer.R;
import com.example.mobileplayer.base.BasePager;
import com.example.mobileplayer.domain.NetMediaItem;

//import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
//import io.reactivex.rxjava3.annotations.NonNull;
//import io.reactivex.rxjava3.core.Observer;
//import io.reactivex.rxjava3.core.Scheduler;
//import io.reactivex.rxjava3.disposables.Disposable;
//import io.reactivex.rxjava3.schedulers.Schedulers;
import java.util.List;

import io.vov.vitamio.utils.Log;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
//import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 网络视频页面
 */
public class NetVideoPager extends BasePager {

    private String url = "http://api.m.mtime.cn/PageSubArea/TrailerList.api";
    private ListView mListView ;
    private TextView tv_nonet;
    private ProgressBar pb_loading;

    public NetVideoPager(Context context) {
        super(context);
    }

    @Override
    public View initView() {
       View view = View.inflate(context , R.layout.netvideo_pager,null);
       mListView = view.findViewById(R.id.net_video_list_view);
       tv_nonet = view.findViewById(R.id.tv_nonet);
       pb_loading = view.findViewById(R.id.pb_loading);
       return view;
    }

    @Override
    public void initData() {
        super.initData();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.m.mtime.cn/")
                .addConverterFactory(GsonConverterFactory.create())
//                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        NetMediaService netService = retrofit.create(NetMediaService.class);
        netService.getData().enqueue(new Callback<List<NetMediaItem>>() {
            @Override
            public void onResponse(Call<List<NetMediaItem>> call, Response<List<NetMediaItem>> response) {
                Log.e(response.toString());
            }

            @Override
            public void onFailure(Call<List<NetMediaItem>> call, Throwable t) {
                Log.e("onFailure-----------");
            }
        });
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<List<NetMediaItem>>() {
//                    @Override
//                    public void onSubscribe(@NonNull Disposable d) {
//
//                    }
//
//                    @Override
//                    public void onNext(@NonNull List<NetMediaItem> netMediaItems) {
//                        Log.e(netMediaItems.toString(),NetMediaService.class );
//                    }
//
//                    @Override
//                    public void onError(@NonNull Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });
    }

}
