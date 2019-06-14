package com.backendme.servicetypes

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.os.PowerManager
import android.os.SystemClock
import android.support.v4.app.NotificationCompat
import android.util.Log


class ForegroundService : Service() {
    private var servicestop = false
    private var wakeLock: PowerManager.WakeLock? = null

    val TAG = "ForegroundService"

    override fun onBind(intent: Intent?): IBinder? {

        return null
    }


    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
            wakeLock = powerManager.newWakeLock(
                PowerManager.PARTIAL_WAKE_LOCK,
                "ExampleApp:Wakelock2"
            )
            wakeLock?.acquire(10 * 60 * 1000L /*10 minutes*/)
            Log.d(TAG, "Wakelock acquired")
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notification = NotificationCompat.Builder(this, MainActivity().FOREGROUND_ID)
            .setContentTitle("Foreground Service")
            .setContentText("Running in Background")
            .setSmallIcon(android.R.drawable.stat_notify_error)
            .build()
        startForeground(2, notification)

        Thread(Runnable {
            for (i in 0 until 500) {
                SystemClock.sleep(1000)
                Log.i(TAG, i.toString())
                if (servicestop) {
                    break
                }

            }
            Log.d(TAG, "Job finished")
        }).start()
        return START_NOT_STICKY

    }


    override fun onDestroy() {
        super.onDestroy()
        servicestop = true
        Log.i(TAG, "Service Stopped")
    }
}