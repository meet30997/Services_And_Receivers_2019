package com.backendme.servicetypes

import android.content.Context
import android.content.Intent
import android.os.SystemClock
import android.support.v4.app.JobIntentService
import android.support.v4.app.NotificationCompat
import android.util.Log

class Jobintentservice : JobIntentService() {
    val TAG = "Jobintentservice"


    fun enqueueWork(context: Context, work: Intent) {
        enqueueWork(context, Jobintentservice::class.java, 123, work)
    }


    override fun onCreate() {
        super.onCreate()
        val notification = NotificationCompat.Builder(this, MainActivity().JOBINTENT_ID)
            .setContentTitle("JOBIntent Service")
            .setContentText("Running in Background")
            .setSmallIcon(android.R.drawable.stat_notify_error)
            .build()
        startForeground(3, notification)
    }

    override fun onHandleWork(p0: Intent) {
        for (i in 0 until 5) {
            SystemClock.sleep(1000)
            Log.i(TAG, i.toString())

        }
    }


}