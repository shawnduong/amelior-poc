package com.hci_g1.amelior

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment

class StepDisplay: Fragment(R.layout.step_display) {
    /** SELF REFERENCE MEMBERS **/
    private var fragView: View? = null

    /** UI METHODS **/
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragView = view
    }

    fun readout(str: String)
    {
        val ro = fragView?.findViewById<TextView>(R.id.readout)
        ro?.text = str
    }

    /** CONNECTING TO THE STEP TRACKER SERVICE **/
    private var STStatus: String? = null
    private var running: Boolean = false
    private var STBound: Boolean = false

    private val STConn = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val binding = service as StepTracker.STBinding
            STStatus = binding.getStepTracker()
            STBound = true
        }

        override fun onServiceDisconnected(name: ComponentName) {
            STBound = false
            readout("StepTracker Killed or Disconnected!")
        }
    }

    private val STIntent = Intent(this@StepDisplay.context, StepTracker::class.java)

    private fun startST() {
        running = (this@StepDisplay.context?.startService(STIntent) != null)
    }

    private fun stopST() {
        this@StepDisplay.context?.stopService(STIntent)
        running = false
    }

    private fun bindST() {
        if(!STBound) {
            this@StepDisplay.context?.bindService(STIntent, STConn, 0)
        }
    }

    private fun unbindST() {
        if(STBound) {
            this@StepDisplay.context?.unbindService(STConn)
            STBound = false
        }
    }

    fun buttonFunc(){
        TODO(reason = "Implement UI controlled start/stop and bind/unbind to StepTacker")
    }
}