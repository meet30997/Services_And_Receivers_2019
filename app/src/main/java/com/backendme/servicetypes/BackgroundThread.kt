package com.backendme.servicetypes

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import kotlinx.android.synthetic.main.backgroud_thread.*

class BackgroundThread : AppCompatActivity() {
    val TAG = "BackgroundThread"

    @Volatile
    var stopthread = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.backgroud_thread)


        start.setOnClickListener {
            stopthread = false

            val thread = Thread(Runnable {

                run {

                    for (i in 0 until 11) {

                        if (stopthread)
                            break




                        runOnUiThread(Runnable {
                            run {

                                start.text = "${i * 10}%"

                            }

                        })

                        if (i == 10) {

                            runOnUiThread(Runnable {
                                run {

                                    start.text = "Start Thread"

                                }

                            })


                        }


                        Log.i(TAG, i.toString())
                        Thread.sleep(1000)
                    }


                }
            })
            thread.start()

        }


        stop.setOnClickListener {
            start.text = "Start Thread"
            stopthread = true

        }
    }

}