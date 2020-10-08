package com.example.startallplayer

import android.annotation.SuppressLint
import android.content.*
import android.os.Bundle
import android.os.IBinder
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import java.util.*

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG  = "MainActivity"
    }

    private lateinit var receiver: MyReceiver
    private lateinit var mBtnStart: Button
    private lateinit var mBtnStop: Button
    private lateinit var mBtnBind: Button
    private lateinit var mBtnUnbind: Button
    private lateinit var mBtnQuery: Button
    private lateinit var mBtnStartDownload: Button
    private lateinit var mTvProgress: TextView
    private lateinit var mDownloadContainer: LinearLayout

    /**
     * 装数据的集合
     */
    private var mediaItems: ArrayList<MediaItem>? = null

    lateinit var downLoadBinder : MyService.DownloadBinder

    private var connection = object : ServiceConnection{

        override fun onServiceDisconnected(name: ComponentName?) {
            Log.d(TAG, "-----onServiceDisconnected------")
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            downLoadBinder = service as MyService.DownloadBinder
            mDownloadContainer.visibility = View.VISIBLE
            Log.d(TAG, "-----onServiceConnected------  : progress: ${downLoadBinder.getProgress()}")
        }

    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val intent = Intent(this, MyService::class.java)

        //动态注册电量广播
        receiver = MyReceiver()
        val intentFiler = IntentFilter()
        //当电量变化的时候会发送这个广播
        intentFiler.addAction(Intent.ACTION_BATTERY_CHANGED)
        registerReceiver(receiver, intentFiler)

//        //注册广播
//        unbindReceiver = UnbindReceiver()
//        val intentFilter = IntentFilter()
//        intentFilter.addAction(MyService.UNBIND)
//        registerReceiver(unbindReceiver, intentFilter)

        mBtnStart = findViewById(R.id.start)
        mBtnStop = findViewById(R.id.stop)
        mBtnBind = findViewById(R.id.bind)
        mBtnUnbind = findViewById(R.id.unbind)
        mBtnQuery = findViewById(R.id.query_media)
        mBtnStartDownload = findViewById(R.id.start_download)
        mTvProgress = findViewById(R.id.progress)
        mDownloadContainer = findViewById(R.id.main_ll_download)

        mBtnStart.setOnClickListener{ startService(intent) }
        mBtnStop.setOnClickListener { stopService(intent) }
        mBtnBind.setOnClickListener { bindService(intent, connection, Context.BIND_AUTO_CREATE) }
        mBtnUnbind.setOnClickListener { unbindService(connection) }
        mBtnQuery.setOnClickListener { getDataFromLocal() }
        mBtnStartDownload.setOnClickListener {
            downLoadBinder.startDownload()
            mTvProgress.text = "${downLoadBinder.getProgress()}%"
        }
    }

    internal class MyReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val level = intent.getIntExtra("level", 0) //0~100;
            Log.d(TAG, "battery: $level")
        }
    }

    /**
     * 从本地的sdcard中得到数据
     * 1.遍历，后缀名
     * 2.从内容提供器中获取视频
     */
    private fun getDataFromLocal() {
        object : Thread() {
            override fun run() {
                super.run()
                mediaItems = ArrayList()
                val resolver: ContentResolver = contentResolver
                val uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                val strs = arrayOf(
                        MediaStore.Video.Media.DISPLAY_NAME,  //视频文件在sdcard中的名称
                        MediaStore.Video.Media.DURATION,  //视频文件的时长
                        MediaStore.Video.Media.SIZE,  //视频文件的大小
                        MediaStore.Video.Media.DATA,  //视频文件的绝对地址
                        MediaStore.Video.Media.ARTIST)
                val cursor = resolver.query(uri, strs, null, null, null)
                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        val mediaItem = MediaItem()
                        mediaItems?.add(mediaItem) //add()操作放在前面可行，里面mediaIem的值在后面改变此处也会改变；
                        val name = cursor.getString(0)
                        mediaItem.name = name
                        val duration = cursor.getLong(1)
                        mediaItem.duration = duration
                        val size = cursor.getLong(2)
                        mediaItem.size = size
                        val data = cursor.getString(3)
                        mediaItem.data = data
                        val artist = cursor.getString(4)
                        mediaItem.artist = artist
                    }
                    cursor.close()
                }
            }
        }.start()
    }


}