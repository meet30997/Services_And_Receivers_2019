package com.backendme.servicetypes

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log

class BroadCastActivity : AppCompatActivity() {
    var Network = NetworkBroadcastReceiver()

    val TAG = "BroadCastActivity"
    var register = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.broadcast_layout)

        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(Network, filter)


    }


    override fun onStart() {
        super.onStart()
        val filters = IntentFilter("No")
        filters.addAction("Yes")
        registerReceiver(snackBroadcastReceiver, filters)
    }


    private val snackBroadcastReceiver = object : BroadcastReceiver() {
        private lateinit var snackbar: Snackbar
        override fun onReceive(context: Context?, intent: Intent?) {
            (context as BroadCastActivity).register = true
            if ("No" == intent?.action) {
                Log.i("snackBroad", "Not Connected")
                snackbar = Snackbar.make(
                    context.findViewById(R.id.cons),
                    "No Internet Connection",
                    Snackbar.LENGTH_INDEFINITE
                )
                snackbar.view.setBackgroundColor(Color.parseColor("#FF0000"))

                snackbar.show()
            }

            if ("Yes" == intent?.action) {
                // Log.i("snackBroad", "Connected")
                snackbar = Snackbar.make(
                    context.findViewById(R.id.cons),
                    "Internet Is Available",
                    Snackbar.LENGTH_INDEFINITE
                )
                snackbar.view.setBackgroundColor(Color.parseColor("#007ED9"))
                snackbar.show()
            }


        }


    }


    override fun onDestroy() {
        super.onDestroy()


        unregisterReceiver(Network)

    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(snackBroadcastReceiver)

    }


}


