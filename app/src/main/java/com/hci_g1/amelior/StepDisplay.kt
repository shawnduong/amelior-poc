package com.hci_g1.amelior

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment

class StepDisplay: Fragment(R.layout.step_display) {
    /** UI METHODS **/
    fun readout(str: String)
    {
        TODO(reason = "Implement onCreateView so that View methods work.")
        //val ro = findViewById<TextView>(R.id.readout)
        //ro.text = str
    }

    /** BINDING THE STEP TRACKER SERVICE **/
    private var localStepTracker: StepTracker? = null
    private var STBound: Boolean = false

    private val STConn = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val binding = service as StepTracker.STBinding;
            localStepTracker = binding.getStepTracker()
            STBound = true
        }

        override fun onServiceDisconnected(name: ComponentName) {
            readout("StepTracker Killed or Disconnected!")
        }
    }
}