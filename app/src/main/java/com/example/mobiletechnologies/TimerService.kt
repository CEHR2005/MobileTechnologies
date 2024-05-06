package com.example.mobiletechnologies

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log

class TimerService : Service() {
    private var handler: Handler? = null
    private var runnable: Runnable? = null
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        handler = Handler(Looper.getMainLooper())
        Log.d(TAG, "Service created")
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.d(TAG, "Service started")
        startTimer()
        return START_STICKY
    }

    private fun startTimer() {
        runnable = object : Runnable {
            override fun run() {
                // Log each time the timer runs
                Log.d(TAG, "Timer tick")
                sendUpdateBroadcast()
                handler!!.postDelayed(this, 1000) // Run every second
            }
        }
        handler!!.postDelayed(runnable as Runnable, 1000)
        Log.d(TAG, "Timer started")
    }

    private fun sendUpdateBroadcast() {
        Log.d(TAG, "Broadcasting timer update")
        val intent = Intent("TIMER_UPDATE")
        sendBroadcast(intent)
    }

    override fun onDestroy() {
        if (runnable != null) {
            handler!!.removeCallbacks(runnable!!)
            Log.d(TAG, "Timer stopped")
        }
        Log.d(TAG, "Service destroyed")
        super.onDestroy()
    }

    companion object {
        private const val TAG = "TimerService" // Tag for log messages
    }
}
