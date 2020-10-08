package com.example.startallplayer.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class MyFirstReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Toast.makeText(context, "MyFirstReceiver receive unbind ", Toast.LENGTH_SHORT).show()
    }
}