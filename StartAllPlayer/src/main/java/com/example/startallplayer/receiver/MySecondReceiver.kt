package com.example.startallplayer.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class MySecondReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Toast.makeText(context, "MySecondReceiver receive unbind ", Toast.LENGTH_SHORT).show()
        abortBroadcast()
    }
}