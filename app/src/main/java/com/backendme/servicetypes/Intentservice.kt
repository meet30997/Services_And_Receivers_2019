package com.backendme.servicetypes

import android.app.IntentService
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.PowerManager
import android.os.SystemClock
import android.support.v4.app.NotificationCompat
import android.util.Log


class Intentservice : IntentService("IntentService") {

    val TAG = "Intentservice"
    private var wakeLock: PowerManager.WakeLock? = null

    init {
        setIntentRedelivery(true)
    }

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
            wakeLock = powerManager.newWakeLock(
                PowerManager.PARTIAL_WAKE_LOCK,
                "ExampleApp:Wakelock"
            )
            wakeLock?.acquire(10 * 60 * 1000L /*10 minutes*/)
            Log.d(TAG, "Wakelock acquired")
        }

        val notification = NotificationCompat.Builder(this, MainActivity().INTENTSERVICE_ID)
            .setContentTitle("Intent Service")
            .setContentText("Running in Background")
            .setSmallIcon(android.R.drawable.stat_notify_error)
            .build()
        startForeground(1, notification)


        setIntentRedelivery(true)
    }


    override fun onHandleIntent(intent: Intent?) {
        Log.i(TAG, "onHandled Intent")
        for (i in 0 until 5) {
            SystemClock.sleep(1000)
            Log.i(TAG, i.toString())

        }
    }


}