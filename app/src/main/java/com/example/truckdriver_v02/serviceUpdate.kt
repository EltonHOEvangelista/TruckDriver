package com.example.truckdriver_v02

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

class serviceUpdate : Service() {
    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Thread {
            while (true) {
                //calling method to transmit data to the server
                TransmitDadaToServer()
                Log.e("Service", "Service is running.")
                try {
                    Thread.sleep(10000)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }.start()

        return super.onStartCommand(intent, flags, startId)
    }

    private fun TransmitDadaToServer() {
        //calling method to transmit data to the server
        //homeFragment.TransmitDataToServer();
        /*
        SomeAsyncOperation.init(new Callback() {
            @Override
            public void onSuccess(HomeFragment initializedInstance) {
                homeFragment = initializedInstance;
                // Now it's safe to access remoteClassInstance
            }

            @Override
            public void onFailure() {
                // Handle failure
            }
        });
        */
    }

    override fun onBind(intent: Intent): IBinder? {
        // TODO: Return the communication channel to the service.
        throw UnsupportedOperationException("Not yet implemented")
    }
}