package com.hci_g1.amelior

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder

/* NOTE: Services use the main thread by default
*   May need to start a thread here so we don't accidentally block UI operations
*   See https://developer.android.com/guide/components/services#CreatingAService
*/

class StepTracker : Service() {
    /** DATA **/
    private var status: String = "Lorem Ipsum"
    private var demand: Int = 0

    private fun getStatus(): String
    {
        return status
    }

    /** INTERFACE **/
    // Provide the status string to clients
    inner class STBinding : Binder()
    {
        fun getStepTracker() : String { return this@StepTracker.getStatus() }
    }

    // Instantiate the interface to return it to clients
    private val binding = STBinding()

    /** LIFECYCLE CALLBACKS **/
    override fun onCreate() {
        super.onCreate()
        status = "Created new StepTracker\n"
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        TODO(reason = "Implement startService in clients")
        TODO(reason = "Implement stop conditions!" +
                "Either stopSelf() locally" +
                "Or stopService() in clients")
        return super.onStartCommand(intent, flags, startId)

        status += "Started StepTracker (request: $startId)\n"

        TODO(reason = "Implement safe start-ups")
        /*
            flags and startId can be used to gracefully handle
            our service getting killed while starting.
            Come back around after main functionality is done.
         */

        // We want the Step Tracking to continue ASAP if it gets killed w/o a manual stop
        return START_REDELIVER_INTENT
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    /** BINDING CALLBACKS **/
    override fun onBind(intent: Intent?): IBinder? {
        demand++
        status += "New Client\nNow bound to $demand clients\n"

        return binding
    }

    override fun onUnbind(intent: Intent?): Boolean {
        demand--
        status = "Client Unbound\nNow bound to $demand clients\n"

        //return super.onUnbind(intent)
        return true
    }

    override fun onRebind(intent: Intent?) {
        demand++
        status = "Client Rebound\nNow bound to $demand clients\n"

        super.onRebind(intent)
    }
}