package com.backendme.servicetypes

import android.annotation.TargetApi
import android.app.*
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import java.util.*


class MainActivity : AppCompatActivity() {


    private var mcurrentTime: Calendar? = null
    val INTENTSERVICE_ID = "IntentService"
    val FOREGROUND_ID = "ForegroundService"
    val JOBINTENT_ID = "JobIntentService"
    val JOB_SERVICE_ID = "Jobservice"
    val TAG = "MainActivity"
    val alarmBroadcastReceiver = AlarmBroadcastReceiver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mcurrentTime = Calendar.getInstance()

    }

    fun startIntentService(v: View) {
        createnotificationchannel(INTENTSERVICE_ID, "intentservice")
        ContextCompat.startForegroundService(this, Intent(this, Intentservice::class.java))
    }


    fun startForegroundService(v: View) {
        createnotificationchannel(FOREGROUND_ID, "forgroundservice")
        ContextCompat.startForegroundService(this, Intent(this, ForegroundService::class.java))

    }

    fun StopForegroundService(v: View) {
        stopService(Intent(this, ForegroundService::class.java))

    }


    fun startJobIntentService(v: View) {
        createnotificationchannel(JOBINTENT_ID, "jobintentservice")
        Jobintentservice().enqueueWork(this, Intent(this, Jobintentservice::class.java))

    }


    fun startJobService(v: View) {

        createnotificationchannel(JOB_SERVICE_ID, "jobservice")
        val componentName = ComponentName(this, Jobservice::class.java)
        val info = JobInfo.Builder(456, componentName)
            .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
            .setPersisted(true)
            .setPeriodic((15 * 60 * 1000).toLong())
            .build()

        val scheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        val resultCode = scheduler.schedule(info)
        if (resultCode == JobScheduler.RESULT_SUCCESS) {
            Log.d(TAG, "Job scheduled")
        } else {
            Log.d(TAG, "Job scheduling failed")
        }
    }


    fun stopJobService(v: View) {
        val scheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        scheduler.cancel(456)
        Log.d(TAG, "Job cancelled")


    }

    fun startBroadcast(v: View) {

        startActivity(Intent(this, BroadCastActivity::class.java))

    }


    fun showtime(v: View) {
        mcurrentTime = Calendar.getInstance()
        val hour = mcurrentTime?.get(Calendar.HOUR_OF_DAY)
        val minutes = mcurrentTime?.get(Calendar.MINUTE)

        val d = TimePickerDialog(this, TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
            mcurrentTime?.set(Calendar.HOUR, hourOfDay)
            mcurrentTime?.set(Calendar.MINUTE, minute)

        }, hour!!, minutes!!, true)

        d.show()

    }


    fun startAlarmService(v: View) {

        Toast.makeText(this, "Alarm set to :${mcurrentTime!!.time}", Toast.LENGTH_SHORT).show()
        Log.i("Alarmmm", "Alarm set to :${mcurrentTime!!.time}")
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(applicationContext, AlarmBroadcastReceiver::class.java)
        intent.action = "com.backendme.servicetypes.Alarm"
        val pendingIntent = PendingIntent.getBroadcast(applicationContext, 0, intent, 0)


        alarmManager.cancel(pendingIntent)

        val SDK_INT = Build.VERSION.SDK_INT
        if (SDK_INT < Build.VERSION_CODES.KITKAT)
            alarmManager.set(AlarmManager.RTC_WAKEUP, mcurrentTime!!.timeInMillis, pendingIntent)
        else if (Build.VERSION_CODES.KITKAT <= SDK_INT && SDK_INT < Build.VERSION_CODES.M)
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, mcurrentTime!!.timeInMillis, pendingIntent)
        else if (SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, mcurrentTime!!.timeInMillis, pendingIntent)
        }


    }

    fun stopBroadcast(v: View) {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(applicationContext, AlarmBroadcastReceiver::class.java)
        intent.action = "com.backendme.servicetypes.Alarm"
        val pendingIntent = PendingIntent.getBroadcast(applicationContext, 0, intent, 0)
        alarmManager.cancel(pendingIntent)
        Log.i("Alarmmm", "Alarm Canceled")
        Toast.makeText(this, "Alarm Canceled", Toast.LENGTH_SHORT).show()

    }


    fun createThread(v: View) {
        startActivity(Intent(this, BackgroundThread::class.java))

    }

    @TargetApi(Build.VERSION_CODES.O)
    private fun createnotificationchannel(id: String, name: String) {
        val channel = NotificationChannel(id, name, NotificationManager.IMPORTANCE_DEFAULT)
        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)
    }


}
