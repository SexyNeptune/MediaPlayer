package com.example.mobileplayer.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobileplayer.R;
import com.example.mobileplayer.domain.MediaItem;
import com.example.mobileplayer.utils.LogUtil;
import com.example.mobileplayer.utils.Utils;
import com.example.mobileplayer.view.VideoView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import io.vov.vitamio.utils.Log;

/**
 * 系统播放器
 */
public class SystemVideoPlayer extends Activity implements View.OnClickListener {
    /**
     * 是否调用系统的监听卡
     * 根据不同的视频格式设置true或false
     */
    private boolean isUseSystem = true;
    /**
     * 视频进度跟进
     */
    private static final int PROGRESS = 1;

    /**
     * 隐藏控制面板
     */
    private static final int HIDE_MEDIACONTROLLER = 2;
    /**
     * 显示网速
     */
    private static final int SHOW_SPEED = 3;

    private static final int DEFAULT = 1;
    private static final int FULL = 2;

    private VideoView videoView;
    private Uri uri;
    private LinearLayout llTop;
    private TextView tvName;
    private ImageView ivBattery;
    private TextView tvSystemTime;
    private Button btnVoice;
    private SeekBar seekbarVoice;
    private Button btnSwitchPlayer;
    private LinearLayout llBottom;
    private RelativeLayout media_controller;
    private TextView tvCurrent;
    private SeekBar seekbarPlayer;
    private TextView tvDuration;
    private Button btnExit;
    private Button btnVideoPre;
    private Button btnVideoStartPause;
    private Button btnVideoNext;
    private Button btnVideoSiwchScreen;
    private LinearLayout ll_buffer;
    private TextView tv_buffer_netspeed;
    private LinearLayout ll_loading;
    private TextView tv_laoding_netspeed;

    private Utils utils = new Utils();

    /**
     * 监听电量变化的广播
     */
    private MyReceiver receiver;
    /**
     * 传进来的视频列表
     */
    private ArrayList<MediaItem> mediaItems;
    /**
     * 要播放的视频
     */
    private int position;
    /**
     * 定义手势识别器
     */
    private GestureDetector detector;
    /**
     * 是否显示控制面板
     */
    private boolean isshowMediaController = false;
    /**
     * 是否全屏
     */
    private boolean isFullScreen = false;
    /**
     *屏幕的高
     */
    private int screanHeight = 0;
    /**
     * 屏幕的宽
     */
    private int screanWidth = 0;
    /**
     * 视频最终的宽
     */
    private int videoWidth = 0;
    /**
     * 视频最终的高
     */
    private int videoHeight = 0;
    /**
     * 音量管理
     */
    private AudioManager audioManager;
    /**
     * 当前音量
     */
    private int currentVoice;
    /**
     * 最大音量
     */
    private int maxVoice;
    /**
     * 是否静音
     */
    private boolean isMute = false;
    /**
     * 是否联网
     */
    private boolean isNetUri;
    /**
     * 上一个播放进度
     */
    private int precurrentPosition=0 ;

    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2018-12-19 17:37:00 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews() {
        setContentView(R.layout.acitvity_system_video_player);
        btnVoice = findViewById( R.id.btn_voice );
        videoView = findViewById(R.id.videoview);
        llTop = findViewById( R.id.ll_top );
        tvName = findViewById( R.id.tv_name );
        ivBattery = findViewById( R.id.iv_battery );
        tvSystemTime = findViewById( R.id.tv_system_time );
        seekbarVoice = findViewById( R.id.seekbar_voice );
        btnSwitchPlayer = findViewById( R.id.btn_switch_player );
        llBottom = findViewById( R.id.ll_bottom );
        tvCurrent = findViewById( R.id.tv_current );
        seekbarPlayer = findViewById( R.id.seekbar_player );
        tvDuration = findViewById( R.id.tv_duration );
        btnExit = findViewById( R.id.btn_exit );
        btnVideoPre = findViewById( R.id.btn_video_pre );
        btnVideoStartPause = findViewById( R.id.btn_video_start_pause );
        btnVideoNext = findViewById( R.id.btn_video_next );
        btnVideoSiwchScreen = findViewById( R.id.btn_video_siwch_screen );
        media_controller = findViewById(R.id.media_controller);
        ll_buffer = findViewById(R.id.ll_buffer);
        tv_buffer_netspeed = findViewById(R.id.tv_buffer_netspeed);
        ll_loading = findViewById(R.id.ll_loading);
        tv_laoding_netspeed = findViewById(R.id.tv_laoding_netspeed);


        btnVoice.setOnClickListener( this );
        btnSwitchPlayer.setOnClickListener( this );
        btnExit.setOnClickListener( this );
        btnVideoPre.setOnClickListener( this );
        btnVideoStartPause.setOnClickListener( this );
        btnVideoNext.setOnClickListener( this );
        btnVideoSiwchScreen.setOnClickListener( this );

        seekbarVoice.setMax(maxVoice);
        seekbarVoice.setProgress(currentVoice);

//        handler.sendEmptyMessage(SHOW_SPEED);
    }

    /**
     * Handle button click events<br />
     * <br />
     * Auto-created on 2018-12-19 17:37:00 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    @Override
    public void onClick(View v) {
        if ( v == btnVoice ) {
            // Handle clicks for btnVoice
            isMute = !isMute;
            updateVoice(currentVoice,isMute);
        } else if ( v == btnSwitchPlayer ) {
            // Handle clicks for btnSwitchPlayer
            showSwichPlayerDialog();
        } else if ( v == btnExit ) {
            // Handle clicks for btnExit
        } else if ( v == btnVideoPre ) {
            // Handle clicks for btnVideoPre
            playPreVideo();
        } else if ( v == btnVideoStartPause ) {
            // Handle clicks for btnVideoStartPause
           startAndPause();
        } else if ( v == btnVideoNext ) {
            // Handle clicks for btnVideoNext
            playNextVideo();
        } else if ( v == btnVideoSiwchScreen ) {
            // Handle clicks for btnVideoSiwchScreen
            setScreanDefaultAndFull();
        }
        handler.removeMessages(HIDE_MEDIACONTROLLER);
        handler.sendEmptyMessageDelayed(HIDE_MEDIACONTROLLER,4000);
        LogUtil.e("onClick ------------");
    }

    private void showSwichPlayerDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("系统播放器提醒您");
        builder.setMessage("当您播放视频，有声音没有画面的时候，请切换万能播放器播放");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startVitamioPlayer();
            }
        });
        builder.setNegativeButton("取消",null);
        builder.show();
    }

    private void startAndPause() {
        if(videoView.isPlaying()){
            //现在视频在播放，设置暂停
            videoView.pause();
            //按钮状态设置为播放
            btnVideoStartPause.setBackgroundResource(R.drawable.btn_video_start_selector);
        }else{
            //现在视频已经暂停，开始播放
            videoView.start();
            //按钮状态设置为暂停
            btnVideoStartPause.setBackgroundResource(R.drawable.btn_video_pause_selector);
        }
    }

    /**
     * 播放上一个视频
     */
    private void playPreVideo() {
        if(mediaItems !=null && mediaItems.size()>0 ){
            position--;
            if (position < mediaItems.size()&& position >=0 ){
                MediaItem mediaItem = mediaItems.get(position);
                isNetUri = utils.isNetUri(mediaItem.getData());
                tvName.setText(mediaItem.getName());
                videoView.setVideoPath(mediaItem.getData());
                //设置按钮属性
                setButtonState();
            }
        }else if(uri != null){
            //上一个和下一个按钮为灰色不可点击
            setButtonState();
        }
    }

    /**
     * 播放下一个视频
     */
    private void playNextVideo() {
        if(mediaItems !=null && mediaItems.size()>0 ){
            position++;
            if (position< mediaItems.size()&& position >=0){
            MediaItem mediaItem = mediaItems.get(position);
            isNetUri = utils.isNetUri(mediaItem.getData());
            tvName.setText(mediaItem.getName());
            videoView.setVideoPath(mediaItem.getData());
            //设置按钮属性
            setButtonState();
            }
        }else if(uri != null){
            //上一个和下一个按钮为灰色不可点击
            setButtonState();
        }
    }

    private void setButtonState() {
        if(mediaItems !=null && mediaItems.size()>0 ){
            if(mediaItems.size() == 1){
                setEnable(false);
            }else if(mediaItems.size() ==2 ){
                if(position==0){
                    btnVideoPre.setBackgroundResource(R.drawable.btn_pre_gray);
                    btnVideoPre.setEnabled(false);
                    btnVideoNext.setBackgroundResource(R.drawable.btn_video_next_selector);
                    btnVideoNext.setEnabled(true);
                }else if(position ==mediaItems.size() - 1){
                    btnVideoPre.setBackgroundResource(R.drawable.btn_video_pre_selector);
                    btnVideoPre.setEnabled(true);
                    btnVideoNext.setBackgroundResource(R.drawable.btn_next_gray);
                    btnVideoNext.setEnabled(false);
                }
            }else {
                if(position==0){
                    btnVideoPre.setBackgroundResource(R.drawable.btn_pre_gray);
                    btnVideoPre.setEnabled(false);
                }else if(position ==mediaItems.size() - 1){
                    btnVideoNext.setBackgroundResource(R.drawable.btn_next_gray);
                    btnVideoNext.setEnabled(false);
                }else{
                    setEnable(true);
                }
            }
        }else if(uri != null){
            setEnable(false);
        }
    }

    public void setEnable(boolean isEnable){
        if (isEnable){
            //两个按钮设置回可选中状态
            btnVideoPre.setBackgroundResource(R.drawable.btn_video_pre_selector);
            btnVideoPre.setEnabled(true);
            btnVideoNext.setBackgroundResource(R.drawable.btn_video_next_selector);
            btnVideoNext.setEnabled(true);
        }else{
            //两个按钮都设置成灰色
            btnVideoPre.setBackgroundResource(R.drawable.btn_pre_gray);
            btnVideoPre.setEnabled(false);
            btnVideoNext.setBackgroundResource(R.drawable.btn_next_gray);
            btnVideoNext.setEnabled(false);
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case SHOW_SPEED:
                    //得到网速
                    String netSpeed = utils.getNetSpeed(SystemVideoPlayer.this);

                    //显示网络速
                    LogUtil.e("网速为："+ netSpeed);
                    tv_laoding_netspeed.setText("玩命加载中..."+netSpeed);
                    tv_buffer_netspeed.setText("缓存中..."+netSpeed);

                    //3、每两秒更新一次
                    removeMessages(SHOW_SPEED);
                    sendEmptyMessageDelayed(SHOW_SPEED,2000);
                case HIDE_MEDIACONTROLLER:
                    hideMediaController();
                case PROGRESS :
                    //1、得到当前视频的播放进度
                    int currentPosition = videoView.getCurrentPosition();

                    //2、设置当前进度
                    seekbarPlayer.setProgress(currentPosition);

                    //设置文本播放进度
                    tvCurrent.setText(utils.stringForTime(currentPosition));

                    //设置系统时间
                    tvSystemTime.setText(getSystemTime());

                    //缓冲进度的更新
                    if (isNetUri) {
                        //只有网络资源才有缓存效果
                        int buffer = videoView.getBufferPercentage();//0~100
                        int totalBuffer = buffer * seekbarPlayer.getMax();
                        int secondaryProgress = totalBuffer / 100;
                        seekbarPlayer.setSecondaryProgress(secondaryProgress);
                    } else {
                        //本地视频没有缓冲效果
                        seekbarPlayer.setSecondaryProgress(0);
                    }

                    //监听卡
                    if (!isUseSystem) {
                        if(videoView.isPlaying()){
                            int buffer = currentPosition - precurrentPosition;
                            if (buffer < 500) {
                                //视频卡了
                                ll_buffer.setVisibility(View.VISIBLE);
                            } else {
                                //视频不卡了
                                ll_buffer.setVisibility(View.GONE);
                            }
                        }else{
                            ll_buffer.setVisibility(View.GONE);
                        }
                    }

                    precurrentPosition = currentPosition;

                    //3、每秒更新一次
                    removeMessages(PROGRESS);
                    sendEmptyMessageDelayed(PROGRESS,1000);
                break;
                default:
                    break;
            }
        }
    };

    /**
     * 得到系统时间
     * @return
     */
    public String getSystemTime(){
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        return format.format(new Date());
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.e("onCreate------");

        initData();
        findViews();
        setListener();
        getData();
        setData();


        //设置控制面板
//        videoView.setMediaController(new MediaController(this));
    }

    private void setData(){
        if (mediaItems != null && mediaItems.size() > 0) {
            MediaItem mediaItem = mediaItems.get(position);
            tvName.setText(mediaItem.getName());//设置视频的名称
            isNetUri = utils.isNetUri(mediaItem.getData());
            videoView.setVideoPath(mediaItem.getData());
        } else if (uri != null) {
            tvName.setText(uri.toString());//设置视频的名称
            isNetUri = utils.isNetUri(uri.toString());
            videoView.setVideoURI(uri);
        } else {
            Toast.makeText(SystemVideoPlayer.this, "帅哥你没有传递数据", Toast.LENGTH_SHORT).show();
        }
        setButtonState();
    }

    private void getData() {
        //得到播放地址
        uri = getIntent().getData();//文件夹，图片浏览器，QQ空间
        mediaItems = (ArrayList<MediaItem>) getIntent().getSerializableExtra("videolist");
        position = getIntent().getIntExtra("position", 0);
    }

    private void initData() {
        utils = new Utils();
        //注册电量广播
        receiver = new MyReceiver();
        IntentFilter intentFiler = new IntentFilter();
        //当电量变化的时候发这个广播
        intentFiler.addAction(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(receiver, intentFiler);

        //创建手势识别器
        detector = new GestureDetector(this,new MySimpleOnGestureListener());

        //得到视频的宽和高de过时方法
//        screanHeight = getWindowManager().getDefaultDisplay().getHeight();
//        screanWidth = getWindowManager().getDefaultDisplay().getWidth();

        //得到视频的宽和高de新的方法
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screanWidth = displayMetrics.widthPixels;
        screanHeight = displayMetrics.heightPixels;

        //得到音量
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        currentVoice = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        maxVoice = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

    }

    class MySimpleOnGestureListener extends GestureDetector.SimpleOnGestureListener{
        @Override//单击
        public boolean onSingleTapConfirmed(MotionEvent e) {
            if (isshowMediaController) {
                //隐藏
                hideMediaController();
                //把隐藏消息移除
//                handler.removeMessages(HIDE_MEDIACONTROLLER);
            } else {
                //显示
                showMediaController();
                //发消息隐藏
//                handler.sendEmptyMessageDelayed(HIDE_MEDIACONTROLLER ,4000);
            }
            LogUtil.e("onSingleTapConfirmed --------------");
            return super.onSingleTapConfirmed(e);
        }

        @Override//长按
        public void onLongPress(MotionEvent e) {
            super.onLongPress(e);
            startAndPause();
            LogUtil.e("onLongPress --------------");
        }

        @Override//双击
        public boolean onDoubleTap(MotionEvent e) {
            setScreanDefaultAndFull();
            LogUtil.e("onDoubleTap --------------");
            return super.onDoubleTap(e);
        }
    }

    private void setScreanDefaultAndFull() {
        if(isFullScreen){
            //默认
            setVideoType(DEFAULT);
            isFullScreen = false;
        }else {
            //全屏
            setVideoType(FULL);
            isFullScreen = true;
        }
    }

    private void setVideoType(int aDefault) {
        switch (aDefault){
            case FULL:
                //全屏化
                //videoView窗口设置
                videoView.setVideoSize(screanWidth,screanHeight);
                //按钮设置
                btnVideoSiwchScreen.setBackgroundResource(R.drawable.btn_video_siwch_screen_full_selector);
                break;
            case DEFAULT:
                //默认化
                //视频的宽和高
                int mVideoWidth = videoWidth;
                int mVideoHeight= videoHeight;
                //屏幕的宽高
                int height = screanHeight;
                int width = screanWidth;
                // for compatibility, we adjust size based on aspect ratio
                if ( mVideoWidth * height  < width * mVideoHeight ) {
                    //Log.i("@@@", "image too wide, correcting");
                    width = height * mVideoWidth / mVideoHeight;
                } else if ( mVideoWidth * height  > width * mVideoHeight ) {
                    //Log.i("@@@", "image too tall, correcting");
                    height = width * mVideoHeight / mVideoWidth;
                }
                videoView.setVideoSize(width,height);
                btnVideoSiwchScreen.setBackgroundResource(R.drawable.btn_video_siwch_screen_default_selector);
                break;
                default:
                    break;
        }
    }

    class MyReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            int level = intent.getIntExtra("level", 0);//0~100;
            setBattery(level);
        }
    }

    private void setBattery(int level) {
        if (level <= 0) {
            ivBattery.setImageResource(R.drawable.ic_battery_0);
        } else if (level <= 10) {
            ivBattery.setImageResource(R.drawable.ic_battery_10);
        } else if (level <= 20) {
            ivBattery.setImageResource(R.drawable.ic_battery_20);
        } else if (level <= 40) {
            ivBattery.setImageResource(R.drawable.ic_battery_40);
        } else if (level <= 60) {
            ivBattery.setImageResource(R.drawable.ic_battery_60);
        } else if (level <= 80) {
            ivBattery.setImageResource(R.drawable.ic_battery_80);
        } else if (level <= 100) {
            ivBattery.setImageResource(R.drawable.ic_battery_100);
        } else {
            ivBattery.setImageResource(R.drawable.ic_battery_100);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void setListener(){
        //准备好的监听
        videoView.setOnPreparedListener(new MyOnPreparedListener());

        //播放出错的监听
        videoView.setOnErrorListener(new MyOnErrorListener());

        //播放完成的监听
        videoView.setOnCompletionListener(new MyOnCompletionListener());

        //设置SeeKbar状态变化的监听
        seekbarPlayer.setOnSeekBarChangeListener(new VideoOnSeekBarChangeListener());

        //监听视频播放卡
        if (isUseSystem) {
            //监听视频播放卡-系统的api
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                videoView.setOnInfoListener(new MyOnInfoListener());
            }
        }

        seekbarVoice.setOnSeekBarChangeListener(new VoiceOnSeekBarChangeListener());
    }

    class MyOnInfoListener implements MediaPlayer.OnInfoListener {

        @Override
        public boolean onInfo(MediaPlayer mp, int what, int extra) {
            switch (what) {
                case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                    ll_buffer.setVisibility(View.VISIBLE);
                    break;
                case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                    ll_buffer.setVisibility(View.GONE);
                    break;
            }
            return true;
        }
    }

    /**
     * 音量seekbar监听器
     */
    class VoiceOnSeekBarChangeListener implements  SeekBar.OnSeekBarChangeListener{
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser) {
                isMute = false;
                updateVoice(progress,false);
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) { }
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) { }
    }

    private void updateVoice(int progress,boolean isMute) {
        if (isMute){
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
            seekbarVoice.setProgress(0);
        }else{
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
            seekbarVoice.setProgress(progress);
            currentVoice = progress;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
                if (isMute) {
                    updateVoice(0,false);
                    isMute = !isMute;
                }
                currentVoice ++;
                updateVoice(currentVoice,false);
                handler.removeMessages(HIDE_MEDIACONTROLLER);
                handler.sendEmptyMessageDelayed(HIDE_MEDIACONTROLLER, 4000);
                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                currentVoice--;
                updateVoice(currentVoice,false);
                handler.removeMessages(HIDE_MEDIACONTROLLER);
                handler.sendEmptyMessageDelayed(HIDE_MEDIACONTROLLER, 4000);
                return true;
                default:
                    break;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 视频进度seekbar监听器
     */
    class VideoOnSeekBarChangeListener implements  SeekBar.OnSeekBarChangeListener{

        /**
         *当手指滑动的时候，会引起SeekBar进度变化，会回调这个方法
         *
         * @param seekBar
         * @param progress
         * @param fromUser  如果是用户引起的true，不是的false
         */
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if(fromUser){
                videoView.seekTo(progress);
            }
        }

        /**
         * 当手指触碰的时候回调这个方法
         *
         * @param seekBar
         */
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) { handler.removeMessages(HIDE_MEDIACONTROLLER);}

        /**
         * 当手指离开的时候回调这个方法
         *
         * @param seekBar
         */
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {handler .sendEmptyMessageDelayed(HIDE_MEDIACONTROLLER,4000);}
    }

    class MyOnPreparedListener implements MediaPlayer.OnPreparedListener {
            //当底层解码准备好的时候
            @Override
            public void onPrepared(MediaPlayer mp) {
                videoWidth = mp.getVideoWidth();
                videoHeight = mp.getVideoHeight();
                videoView.start();//开始播放
                //视频的总时长
                int duration = videoView.getDuration();
                seekbarPlayer.setMax(duration);
                tvDuration.setText(utils.stringForTime(duration));
                //默认隐藏控制面板
                hideMediaController();
                //默认视频的宽高
                setVideoType(DEFAULT);
                //把加载页面消失掉
                ll_loading.setVisibility(View.GONE);
                //发消息
                handler.sendEmptyMessage(PROGRESS);
            }
    }

    class MyOnErrorListener implements MediaPlayer.OnErrorListener {

        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
//            Toast.makeText(SystemVideoPlayer.this, "播放出错了哦", Toast.LENGTH_SHORT).show();
            //1.播放的视频格式不支持--跳转到万能播放器继续播放
            startVitamioPlayer();
            //2.播放网络视频的时候，网络中断---1.如果网络确实断了，可以提示用于网络断了；2.网络断断续续的，重新播放
            //3.播放的时候本地文件中间有空白---下载做完成
            return true;
        }
    }

    /**
     * a,把数据按照原样传入VtaimoVideoPlayer播放器
     b,关闭系统播放器
     */
    private void startVitamioPlayer() {

        if(videoView != null){
            videoView.stopPlayback();
        }

        Intent intent = new Intent(this,VitamioVideoPlayer.class);
        if(mediaItems != null && mediaItems.size() > 0){

            Bundle bundle = new Bundle();
            bundle.putSerializable("videolist", mediaItems);
            intent.putExtras(bundle);
            intent.putExtra("position", position);

        }else if(uri != null){
            intent.setData(uri);
        }
        startActivity(intent);

        finish();//关闭页面
    }

    class MyOnCompletionListener implements MediaPlayer.OnCompletionListener{
        @Override
        public void onCompletion(MediaPlayer mp) {
            playNextVideo();
//            Toast.makeText(SystemVideoPlayer.this, "播放完成了= " + uri , Toast.LENGTH_SHORT).show();
        }
    }

    private float startY;
    /**
     * 屏幕的高
     */
    private float touchRange;

    /**
     * 当一按下的音量
     */
    private int mVol;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        detector.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //1.按下记录值
                startY = event.getY();
                mVol = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                touchRange = Math.min(screanHeight, screanWidth);//screenHeight
                handler.removeMessages(HIDE_MEDIACONTROLLER);
                LogUtil.e("onTouchEvent---------- ACTION_DOWN" );
                break;
            case MotionEvent.ACTION_MOVE:
                float endY = event.getY();
                float distanceY = startY - endY;
                //改变声音 = （滑动屏幕的距离： 总距离）*音量最大值
                float delta = (distanceY / touchRange) * maxVoice;
                //最终声音 = 原来的 + 改变声音；
                int voice = (int) (mVol+delta);
                if (delta != 0) {
                    updateVoice(voice, false);
                }
                LogUtil.e("onTouchEvent---------- ACTION_MOVE" );
                break;
            case MotionEvent.ACTION_UP:
                handler.sendEmptyMessageDelayed(HIDE_MEDIACONTROLLER, 4000);
                LogUtil.e("onTouchEvent---------- ACTION_UP" );
                break;
        }
        Log.e("ada",SystemVideoPlayer.this);
        return super.onTouchEvent(event);
    }

    /**
     * 显示控制面板
     */
    private void showMediaController() {
        media_controller.setVisibility(View.VISIBLE);
        isshowMediaController = true;
    }


    /**
     * 隐藏控制面板
     */
    private void hideMediaController() {
        media_controller.setVisibility(View.GONE);
        isshowMediaController = false;
    }

    @Override
    protected void onStart() {
        LogUtil.e("onStart------");
        super.onStart();
    }

    @Override
    protected void onRestart() {
        LogUtil.e("onRestart------");
        super.onRestart();
    }

    @Override
    protected void onResume() {
        LogUtil.e("onResume------");
        super.onResume();
    }

    @Override
    protected void onPause() {
        LogUtil.e("onPause------");
        super.onPause();
    }

    @Override
    protected void onStop() {
        LogUtil.e("onStop------");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        //释放资源的时候，先释放子类，在释放父类
        if (receiver != null) {
            unregisterReceiver(receiver);
            receiver = null;
        }
        LogUtil.e("onDestroy------");
        super.onDestroy();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
