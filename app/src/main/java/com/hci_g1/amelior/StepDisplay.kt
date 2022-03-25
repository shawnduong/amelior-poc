package com.hci_g1.amelior

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment

class StepDisplay: Fragment(R.layout.step_display) {
    /** UI METHODS **/
    private var fragView: View? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragView = view
    }

    fun readout(str: String)
    {
        val ro = fragView?.findViewById<TextView>(R.id.readout)
        ro?.text = str
    }

    /** BINDING THE STEP TRACKER SERVICE **/
    private var STStatus: String? = null
    private var STBound: Boolean = false

    private val STConn = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val binding = service as StepTracker.STBinding
            STStatus = binding.getStepTracker()
            STBound = true
        }

        override fun onServiceDisconnected(name: ComponentName) {
            readout("StepTracker Killed or Disconnected!")
        }
    }
}