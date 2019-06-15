package com.backendme.servicetypes

import android.annotation.SuppressLint
import android.os.*
import android.util.Log
import java.lang.ref.WeakReference


class Handlerthread : HandlerThread("Handlerthread", Process.THREAD_PRIORITY_BACKGROUND) {

    private var handler: Handler? = null
    private val EXAMPLE_TASK = 2
    private val TAG = "Handlerthread"
    private var mUiThreadCallback: WeakReference<UiCallback>? = null

    @SuppressLint("HandlerLeak")
    override fun onLooperPrepared() {

        handler = object : Handler() {
            override fun handleMessage(msg: Message) {
                when (msg.what) {

                    EXAMPLE_TASK -> {
                        Log.i(TAG, "Message Received For $EXAMPLE_TASK")
                        for (i in 0 until 6) {
                            Log.i(TAG, i.toString() + "For Task 2")
                            SystemClock.sleep(1000)


                            if (i == 4) {
                                if (!Thread.interrupted() && mUiThreadCallback != null && mUiThreadCallback!!.get() != null) {
                                    val msgg = Message()
                                    msgg.what = 5
                                    mUiThreadCallback!!.get()?.publishToUiThread(msgg)

                                }


                            }
                        }
                    }
                }


            }
        }
    }


    fun setUiThreadCallback(callback: UiCallback) {
        this.mUiThreadCallback = WeakReference<UiCallback>(callback)
    }


    fun getadthandler(): Handler? {

        return this.handler
    }


}