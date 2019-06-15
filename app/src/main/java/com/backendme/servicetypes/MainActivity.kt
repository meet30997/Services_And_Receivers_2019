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
import android.os.Handler
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import java.util.*


class MainActivity : AppCompatActivity() {


    val INTENTSERVICE_ID = "IntentService"
    val FOREGROUND_ID = "ForegroundService"
    val JOBINTENT_ID = "JobIntentService"
    val JOB_SERVICE_ID = "Jobservice"
    val TAG = "MainActivity"
    val alarmBroadcastReceiver = AlarmBroadcastReceiver()

    private val handlerforrepeat = Handler()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    fun startIntentService(v: View) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            createnotificationchannel(INTENTSERVICE_ID, "intentservice")
        ContextCompat.startForegroundService(this, Intent(this, Intentservice::class.java))
    }


    fun startForegroundService(v: View) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            createnotificationchannel(FOREGROUND_ID, "forgroundservice")
        ContextCompat.startForegroundService(this, Intent(this, ForegroundService::class.java))

    }

    fun StopForegroundService(v: View) {
        stopService(Intent(this, ForegroundService::class.java))

    }


    fun startJobIntentService(v: View) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            createnotificationchannel(JOBINTENT_ID, "jobintentservice")
        Jobintentservice().enqueueWork(this, Intent(this, Jobintentservice::class.java))

    }


    fun startJobService(v: View) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
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
        val c2 = Calendar.getInstance()
        val hour = c2.get(Calendar.HOUR_OF_DAY)
        val minutes = c2.get(Calendar.MINUTE)


        val d = TimePickerDialog(this, TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
            val c = Calendar.getInstance()
            c.set(Calendar.HOUR_OF_DAY, hourOfDay)
            c.set(Calendar.MINUTE, minute)
            c.set(Calendar.SECOND, 0)
            createalarm(c)
        }, hour, minutes, false)

        d.show()

    }


    private fun createalarm(c: Calendar) {


        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmBroadcastReceiver::class.java)
        intent.action = "com.backendme.servicetypes.Alarm"
        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0)

        if (c.before(Calendar.getInstance())) {
            c.add(Calendar.DATE, 1)
        }


        val SDK_INT = Build.VERSION.SDK_INT
        when {
            SDK_INT < Build.VERSION_CODES.KITKAT -> alarmManager.set(
                AlarmManager.RTC_WAKEUP,
                c.timeInMillis,
                pendingIntent
            )
            Build.VERSION_CODES.KITKAT <= SDK_INT && SDK_INT < Build.VERSION_CODES.M -> alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                c.timeInMillis,
                pendingIntent
            )
            SDK_INT >= Build.VERSION_CODES.M -> alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                c.timeInMillis,
                pendingIntent
            )
        }

        Toast.makeText(this, "Alarm set to :${c.time}", Toast.LENGTH_SHORT).show()
        Log.i("Alarmmm", "Alarm set to :${c.time}")

    }

    fun stopBroadcast(v: View) {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(applicationContext, AlarmBroadcastReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(applicationContext, 0, intent, 0)
        alarmManager.cancel(pendingIntent)
        Log.i("Alarmmm", "Alarm Canceled")
        Toast.makeText(this, "Alarm Canceled", Toast.LENGTH_SHORT).show()

    }


    fun createThread(v: View) {
        startActivity(Intent(this, BackgroundThread::class.java))

    }


    fun handlrethread(v: View) {


        startActivity(Intent(this, HandlerThreadActivity::class.java))


    }

    fun asyncActivity(V: View) {

        startActivity(Intent(this, AsyncTaskActivity::class.java))

    }


    fun startrepeat(v: View) {

        runnable.run()

    }

    fun stoprepeat(v: View) {

        handlerforrepeat.removeCallbacks(runnable)
    }

    private val runnable = object : Runnable {
        override fun run() {
            Toast.makeText(this@MainActivity, "Repeating Toast....", Toast.LENGTH_SHORT).show()
            handlerforrepeat.postDelayed(this, 3000)
        }
    }



    @TargetApi(Build.VERSION_CODES.O)
    private fun createnotificationchannel(id: String, name: String) {
        val channel = NotificationChannel(id, name, NotificationManager.IMPORTANCE_DEFAULT)
        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)
    }


}
