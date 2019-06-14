package com.backendme.servicetypes

import android.app.job.JobParameters
import android.app.job.JobService
import android.os.SystemClock
import android.support.v4.app.NotificationCompat
import android.util.Log

class Jobservice : JobService() {
    private val TAG = "Jobservice"
    private var jobCancelled = false


    override fun onStopJob(params: JobParameters?): Boolean {
        Log.d(TAG, "Job cancelled before completion")
        jobCancelled = true
        return true
    }

    override fun onStartJob(params: JobParameters?): Boolean {

        val notification = NotificationCompat.Builder(this, MainActivity().INTENTSERVICE_ID)
            .setContentTitle("JobService is Service")
            .setContentText("Running in Background")
            .setSmallIcon(android.R.drawable.stat_notify_error)
            .build()
        startForeground(6, notification)

        Thread(Runnable {
            for (i in 0 until 500) {
                SystemClock.sleep(1000)
                Log.i(TAG, i.toString())
                if (jobCancelled) {
                    break
                }

            }
            Log.d(TAG, "Job finished")
            jobFinished(params, false)
        }).start()




        return true
    }


}