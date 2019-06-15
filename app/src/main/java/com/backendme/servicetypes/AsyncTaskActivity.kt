package com.backendme.servicetypes

import android.content.Context
import android.graphics.Color
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.async_layout.*
import java.lang.ref.WeakReference

class AsyncTaskActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.async_layout)
    }


    fun startasynctask(v: View) {

        Asynctask(this).execute(10)

    }

    class Asynctask(context: Context) : AsyncTask<Int, Int, String>() {
        private var weakReference: WeakReference<AsyncTaskActivity>? = null

        init {

            weakReference = WeakReference<AsyncTaskActivity>(context as AsyncTaskActivity?)

        }


        override fun onPreExecute() {
            super.onPreExecute()
            val activity: AsyncTaskActivity = weakReference!!.get()!!

            if (activity.isFinishing || activity == null) {

                return
            }
            activity.progressBar.visibility = View.VISIBLE


        }


        override fun doInBackground(vararg params: Int?): String {

            for (i in 0 until params[0]!!) {
                publishProgress(i * 100 / params[0]!!)
                try {
                    Thread.sleep(1000)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }

            }

            return "AsyncTask Finished!"
        }


        override fun onProgressUpdate(vararg values: Int?) {
            super.onProgressUpdate(*values)
            val activity: AsyncTaskActivity = weakReference!!.get()!!

            if (activity.isFinishing || activity == null) {

                return
            }

            activity.progressBar.progress = values[0]!!
            activity.status.setTextColor(Color.parseColor("#FF0000"))
            activity.status.textSize = 25f
            activity.status.text = "Downloading...."
        }


        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            val activity: AsyncTaskActivity = weakReference!!.get()!!

            if (activity.isFinishing || activity == null) {

                return
            }

            activity.progressBar.progress = 0
            activity.progressBar.visibility = View.INVISIBLE
            activity.status.text = "Download Finished!"
            Toast.makeText(activity, result, Toast.LENGTH_SHORT).show()

        }


    }


}