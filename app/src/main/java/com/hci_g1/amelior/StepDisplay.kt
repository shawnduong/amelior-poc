package com.hci_g1.amelior

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment

class StepDisplay: Fragment(R.layout.step_display) {
    /** SELF REFERENCE MEMBERS **/
    private lateinit var fragContext: Context
    private lateinit var fragView: View

    /** LIFECYCLE CALLBACKS **/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragContext = requireContext()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragView = view
    }

    /** UI METHODS **/
    fun readout(str: String) {
        val ro = fragView.findViewById<TextView>(R.id.readout)
        ro.text = str
    }

    /** CONNECTING TO THE STEP TRACKER SERVICE **/

    private var STStatus: String? = null
    private var STRunning: Boolean = false
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

    // Setting the intent as a global var tries to access context before it exists!
    //private val STIntent = Intent(fragContext, StepTracker::class.java)


    private fun getSTIntent(): Intent = Intent(fragContext, StepTracker::class.java)

    private fun startST() {
        STRunning = (fragContext.startService(getSTIntent()) != null)
    }

    private fun stopST() {
        fragContext.stopService(getSTIntent())
        STRunning = false
    }

    private fun bindST() {
        if(!STBound) {
            fragContext.bindService(getSTIntent(), STConn, 0)
        }
    }

    private fun unbindST() {
        if(STBound) {
            fragContext.unbindService(STConn)
            STBound = false
        }
    }

    /** BUTTON FUNCTIONS **/
    fun toggleStartST(view: View) {
        //TODO(reason = "Implement UI controlled start/stop and bind/unbind to StepTacker")
        val startButton = fragView
            .findViewById<Button>(R.id.toggle_button_start_step_tracker)
        if(STRunning) {
            stopST()
            startButton.text = getString(R.string.start_step_tracker)
        } else {
            startST()
            startButton.text = getString(R.string.stop_step_tracker)
        }
    }

    fun toggleBindST(view: View) {
        val bindButton = fragView
            .findViewById<Button>(R.id.toggle_button_bind_step_tracker)
        if(STRunning) {
            if(STBound) {
                unbindST()
                bindButton.text = getString(R.string.bind_step_tracker)
            } else {
                bindST()
                bindButton.text = getString(R.string.unbind_step_tracker)
            }
        }
    }



}