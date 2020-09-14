package com.example.mobileplayer.activity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.LruCache;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.mobileplayer.R;
import com.example.mobileplayer.domain.MediaItem;
import com.example.mobileplayer.service.MusicPlayerService;
import com.example.mobileplayer.utils.Utils;

import java.util.Calendar;

public class AudioPlayer extends AppCompatActivity implements View.OnClickListener{

    private ImageView ivIcon;
    private Button btnAudioStartPause;
    private TextView tvArtist;
    private TextView tvName;
    private TextView tvTime;
    private SeekBar seekbarAudio;
    private Button btnAudioPlaymode;
    private Button btnAudioPre;
    private Button btnAudioNext;
    private Button btnLyrc;
//    private ShowLyricView showLyricView;
//    private BaseVisualizerView baseVisualizerView;

    private int position;
    /**
     * 进度更新
     */
    private static final int PROGRESS = 1;
    /**
     * 显示歌词
     */
    private static final int SHOW_LYRIC = 2;

    private IMusicPlayerService service;//服务的代理类，通过它可以调用服务的方法
    private BroadcastReceiver receiver;
    private Utils utils ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_player);
        findView();
        initView();
        getData();
        setListener();
        startAndBindService();
    }

    private void setListener() {
        btnAudioStartPause.setOnClickListener(this);
    }

    private void initView() {
        ivIcon.setBackgroundResource(R.drawable.animation_list);
        AnimationDrawable drawable = (AnimationDrawable) ivIcon.getBackground();
        drawable.start();
    }

    private ServiceConnection con = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder iBinder) {
            service = IMusicPlayerService.Stub.asInterface(iBinder);
            if(service != null){
                try {
//                    if(!notification){//从列表
//                        service.openAudio(position);
//                    }else{
//                        System.out.println("onServiceConnected==Thread-name=="+Thread.currentThread().getName());
//                        //从状态栏
//                        showViewData();
//                    }
                    service.openAudio(position);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
            try {
                if(service != null){
                    service.stop();
                    service = null;
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    };

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
                switch (msg.what){
                    case SHOW_LYRIC://显示歌词

                        //1.得到当前的进度
                        try {
                            int currentPosition = service.getCurrentPosition();


                            //2.把进度传入ShowLyricView控件，并且计算该高亮哪一句

//                            showLyricView.setshowNextLyric(currentPosition);
                            //3.实时的发消息
                            handler.removeMessages(SHOW_LYRIC);
                            handler.sendEmptyMessage(SHOW_LYRIC);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }

                        break;
                    case PROGRESS:

                        try {
                            //1.得到当前进度
                            int currentPosition = service.getCurrentPosition();


                            //2.设置SeekBar.setProgress(进度)
                            seekbarAudio.setProgress(currentPosition);

                            //3.时间进度跟新
                            tvTime.setText(utils.stringForTime(currentPosition)+"/"+utils.stringForTime(service.getDuration()));


                            //4.每秒更新一次
                            handler.removeMessages(PROGRESS);
                            handler.sendEmptyMessageDelayed(PROGRESS,1000);



                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                        break;
                }
                return true;
            }
    });

    private void startAndBindService() {
        Intent intent = new Intent(this, MusicPlayerService.class);
        intent.setAction("com.example.mobileplayer_OPENAUDIO");
        bindService(intent, con, Context.BIND_AUTO_CREATE);
        startService(intent);//不至于实例化多个服务
    }

    private void getData() {
        position = getIntent().getIntExtra("position",0);
        //注册广播
        receiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MusicPlayerService.OPENAUDIO);
        registerReceiver(receiver, intentFilter);
    }

    class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            showData();
        }
    }

    //3.订阅方法
//    @Subscribe(threadMode = ThreadMode.MAIN,sticky = false,priority = 0)
    public void showData() {
        //发消息开始歌词同步
        showViewData();
//        showLyric();
//        checkPlaymode();
//        setupVisualizerFxAndUi();

    }

    public void onEventMainThread(MediaItem mediaItem){
        showViewData();
//        showLyric();
//        checkPlaymode();
//        setupVisualizerFxAndUi();
    }

    private void showViewData() {
        try {
            tvArtist.setText(service.getArtist());
            tvName.setText(service.getName());
            //设置进度条的最大值
            seekbarAudio.setMax(service.getDuration());

            //发消息
//            handler.sendEmptyMessage(PROGRESS);

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void findView() {
        ivIcon = findViewById(R.id.iv_icon);
        btnAudioStartPause = findViewById(R.id.btn_audio_start_pause);
        tvArtist = findViewById( R.id.tv_artist );
        tvName = findViewById( R.id.tv_name );
        tvTime = findViewById( R.id.tv_time );
        seekbarAudio = findViewById( R.id.seekbar_audio );
        btnAudioPlaymode = findViewById( R.id.btn_audio_playmode );
        btnAudioPre = findViewById( R.id.btn_audio_pre );
        btnAudioStartPause = findViewById( R.id.btn_audio_start_pause );
        btnAudioNext = findViewById( R.id.btn_audio_next );
        btnLyrc = findViewById( R.id.btn_lyrc );
//        showLyricView = (ShowLyricView) findViewById(R.id.showLyricView);
//        baseVisualizerView = (BaseVisualizerView) findViewById(R.id.baseVisualizerView);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //取消注册广播
        if(receiver != null){
            unregisterReceiver(receiver);
            receiver = null;
        }
    }

    @Override
    public void onClick(View v) {
        if (v == btnAudioStartPause) {
            if (service != null) {
                try {
                    if (service.isPlaying()) {
                        //暂停
                        service.pause();
                        //按钮-播放
                        btnAudioStartPause.setBackgroundResource(R.drawable.btn_audio_start_selector);
                    } else {
                        //播放
                        service.start();
                        //按钮-暂停
                        btnAudioStartPause.setBackgroundResource(R.drawable.btn_audio_pause_selector);
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
