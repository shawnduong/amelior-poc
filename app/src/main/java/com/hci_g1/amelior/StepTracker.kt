package com.hci_g1.amelior

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder

class StepTracker : Service() {
    /** INTERFACE **/
    // Provide a reference to to this StepTracker to clients
    inner class STBinding : Binder()
    {
        fun getStepTracker() : StepTracker { return this@StepTracker }
    }

    // Instantiate the interface to return it to clients
    private val binding = STBinding()

    /** LIFECYCLE CALLBACKS **/
    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        TODO("Implement startService in clients")
        TODO("Implement stop conditions!" +
                "Either stopSelf() locally" +
                "Or stopService() in clients")
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    /** BINDING CALLBACKS **/
    override fun onBind(p0: Intent?): IBinder? {
        TODO("Implement onServiceConnected() in clients")
        return binding
    }

    override fun onUnbind(intent: Intent?): Boolean {
        //return super.onUnbind(intent)
        return true
    }

    override fun onRebind(intent: Intent?) {
        super.onRebind(intent)
    }
}