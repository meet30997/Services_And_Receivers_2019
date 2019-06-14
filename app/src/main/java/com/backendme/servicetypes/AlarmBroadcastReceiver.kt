package com.backendme.servicetypes

import android.annotation.TargetApi
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.util.Log
import android.widget.Toast

class AlarmBroadcastReceiver : BroadcastReceiver() {
    private var manager: NotificationManager? = null
    val ALARM_SERVICE = "alarmbroadcast"

    override fun onReceive(context: Context?, intent: Intent?) {

        if ("com.backendme.servicetypes.Alarm".equals(intent?.action)) {

            Toast.makeText(context, "Alarm Received", Toast.LENGTH_SHORT).show()
            Log.i("Alarmmm", "Alarm Received")
            createnotificationchannel(context, ALARM_SERVICE, "intentservice")

            val notification = NotificationCompat.Builder(context!!, ALARM_SERVICE)
                .setContentTitle("Alarm Service Triggered")
                .setContentText("Wake Up...Boy")
                .setSmallIcon(android.R.drawable.stat_notify_error)
                .build()
            manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            manager!!.notify(8, notification)


        }

    }


    @TargetApi(Build.VERSION_CODES.O)
    private fun createnotificationchannel(context: Context?, id: String, name: String) {
        val channel = NotificationChannel(id, name, NotificationManager.IMPORTANCE_DEFAULT)
        manager = context!!.getSystemService(NotificationManager::class.java)
        manager!!.createNotificationChannel(channel)
    }
}