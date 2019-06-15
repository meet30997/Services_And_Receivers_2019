package com.backendme.servicetypes

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.util.Log

class NetworkBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {

        if (ConnectivityManager.CONNECTIVITY_ACTION == intent!!.action) {
            if (intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false)) {
                val int1 = Intent("No")
                context!!.sendBroadcast(int1)
                Log.i("NetworkBroad", "Connectivity Not Available")
            } else {
                val int1 = Intent("Yes")
                context!!.sendBroadcast(int1)

                Log.i("NetworkBroad", "Connectivity Available")

            }
        }


    }

}