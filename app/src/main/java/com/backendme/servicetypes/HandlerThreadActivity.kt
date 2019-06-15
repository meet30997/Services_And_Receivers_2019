package com.backendme.servicetypes

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.os.SystemClock
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.hadlerthread_layout.*
import java.lang.ref.WeakReference


class HandlerThreadActivity : AppCompatActivity(), UiCallback {
    override fun publishToUiThread(message: Message) {


        if (mUiHandler != null) {
            mUiHandler!!.sendMessage(message)
        }
    }

    private val handlerthread = Handlerthread()
    private val EXAMPLE_TASK = 2
    private val runnable = ExampleRunnable1()
    private var mUiHandler: UiHandler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.hadlerthread_layout)

        mUiHandler = UiHandler(this)

        handlerthread.setUiThreadCallback(this)
        handlerthread.start()
    }


    fun startwork(V: View) {

        val msg = Message.obtain(handlerthread.getadthandler())
        msg.what = EXAMPLE_TASK
        msg.sendToTarget()

        //Let's Start New Runnable Using Our HandlerThread
        // Remember To Initialize Runnable Inner Class on Top

        handlerthread.getadthandler()!!.post(runnable)

    }


    fun removemsg(v: View) {

        //With Remove Method We Can Remove Particular runnable with removeCallbacks() Method
        // We Can Also Stop The Process By removeMessages()


        // handlerthread.getadthandler()!!.removeMessages(EXAMPLE_TASK2)
        handlerthread.getadthandler()!!.removeCallbacks(runnable)

    }


    override fun onDestroy() {
        super.onDestroy()
        // handlerthread.quit()  remove future runnable and stop after completing current dispatched runnable
        handlerthread.quitSafely()
    }


    internal class ExampleRunnable1 : Runnable {

        override fun run() {


            for (i in 0..6) {
                Log.d("HandlerThreadActivity", "Runnable1: $i")
                SystemClock.sleep(1000)


            }
        }
    }


    private class UiHandler(val context: Context) : Handler() {
        private var mWeakRefContext: WeakReference<Context>? = null

        init {

            mWeakRefContext = WeakReference(context)

        }


        @SuppressLint("SetTextI18n")
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                5 -> if (mWeakRefContext != null && mWeakRefContext!!.get() != null) {

                    (context as HandlerThreadActivity).textstart.textSize = 30f
                    context.textstart.setTextColor(Color.parseColor("#FF0000"))
                    (context).textstart.text = "Done"
                    Toast.makeText(mWeakRefContext!!.get(), "Message 1 has been processed", Toast.LENGTH_SHORT).show()
                }


            }
        }
    }

}
