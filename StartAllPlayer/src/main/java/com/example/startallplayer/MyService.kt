package com.example.startallplayer

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import android.util.Log

class MyService : Service() {

    companion object {
        private const val TAG  = "MyService"
        const val UNBIND = "com.example.mobileplayer_UNBIND"
    }

    private var progress : Int = 0

    private val mBinder = DownloadBinder()

    inner class DownloadBinder : Binder() {

        fun startDownload() {
            Log.d(TAG, "----startDownload--- ");
        }

        fun getProgress(): Int {
            return this@MyService.progress
        }
    }

    override fun onBind(intent: Intent): IBinder {
        Log.d(TAG, "----onBind---")
        return mBinder
    }

    override fun onCreate() {
        Log.d(TAG, "----onCreate---")
        progress = (1..100).random()
        startForeground(1, createNotification())
        super.onCreate()
    }

    private fun createNotification(): Notification {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val channel = NotificationChannel("my_service", "前台service通知", NotificationManager.IMPORTANCE_DEFAULT)
            manager.createNotificationChannel(channel)
        }
        val intent = Intent(this, MainActivity::class.java)
        val pi = PendingIntent.getActivity(this, 0, intent, 0)
        return NotificationCompat.Builder(this, "my_service")
                .setContentTitle("下载测试")
                .setContentText("下载进度：$progress%")
                .setContentIntent(pi)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.ic_launcher_foreground))
                .build();
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "----onStartCommand---")
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onUnbind(intent: Intent?): Boolean {
        val unbindIntent = Intent(UNBIND)
        unbindIntent.setPackage(packageName)
//        sendBroadcast(unbindIntent)
        sendOrderedBroadcast(unbindIntent, null)
        Log.d(TAG, "----onUnbind----")
        return super.onUnbind(intent)
    }

    override fun onDestroy() {
        Log.d(TAG, "----onDestroy---")
        super.onDestroy()
    }

}
