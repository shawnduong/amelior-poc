package com.hci_g1.amelior

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.hardware.SensorManager
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

class StepDisplay: Fragment(R.layout.step_display) {
    /** SELF REFERENCE MEMBERS **/
    private lateinit var fragParentContext: Context
    private lateinit var fragContext: Context
    private lateinit var fragView: View

    /** LIFECYCLE CALLBACKS **/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragParentContext = requireActivity()
        fragContext = requireContext()

        // Runtime Permissions
        Log.d("StepDisplay", "Checking Permissions")
        val permission = ContextCompat.checkSelfPermission(fragParentContext,
            Manifest.permission.ACTIVITY_RECOGNITION)
        if(permission == PackageManager.PERMISSION_DENIED) {
            Log.d("StepDisplay", "Requesting Permissions")
            ActivityCompat.requestPermissions(
                fragParentContext as FragmentActivity,
                arrayOf(Manifest.permission.ACTIVITY_RECOGNITION),
                1
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragView = view

        val startButton: Button = fragView.findViewById<Button>(R.id.toggle_button_start_step_tracker)
        startButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                toggleStartST()
            }
        })

        val bindButton: Button = fragView.findViewById<Button>(R.id.toggle_button_bind_step_tracker)
        bindButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                toggleBindST()
            }
        })
    }

    /** UI METHODS **/
    fun readout(str: String?) {
        val ro = fragView.findViewById<TextView>(R.id.readout)
        ro.text = str
    }

    /** CONNECTING TO THE STEP TRACKER SERVICE **/
    private var STStatus: String? = null
    private var stepTrackerRunning: Boolean = false
    private var stepTrackerBound: Boolean = false
    //private var steps: Float = 0f

    private fun readSteps(s:Float): Float {
        Log.d("StepDisplay", "steps read")
        readout("$s")
        return s
    }

    private val STConn = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val binding = service as StepTracker.STBinding
            //STStatus = binding.getStepTrackerStatus() as String
            //binding.stepTrackerCallback(::readSteps) // NOTE: this callback works even after unbinding.
            stepTrackerBound = true
            //readout(STStatus)
        }

        override fun onServiceDisconnected(name: ComponentName) {
            stepTrackerBound = false
            readout("StepTracker Killed or Disconnected!")
        }
    }

    // Setting the intent as a global var tries to access context before it exists!
    //private val STIntent = Intent(fragContext, StepTracker::class.java)

    private fun getSTIntent(): Intent = Intent(fragContext, StepTracker::class.java)

    private fun startST() {
        stepTrackerRunning = (fragContext.startService(getSTIntent()) != null)
    }

    private fun stopST() {
        fragContext.stopService(getSTIntent())
        stepTrackerRunning = false
    }

    private fun bindST() {
        if(!stepTrackerBound) {
            fragContext.bindService(getSTIntent(), STConn, 0)
        }
    }

    private fun unbindST() {
        if(stepTrackerBound) {
            fragContext.unbindService(STConn)
            stepTrackerBound = false
        }
    }

    /** BUTTON FUNCTIONS **/
    fun toggleStartST() {
        val startButton = fragView
            .findViewById<Button>(R.id.toggle_button_start_step_tracker)
        if(stepTrackerRunning) {
            stopST()
            startButton.text = getString(R.string.start_step_tracker)
        } else {
            startST()
            startButton.text = getString(R.string.stop_step_tracker)
        }
    }

    fun toggleBindST() {
        val bindButton = fragView
            .findViewById<Button>(R.id.toggle_button_bind_step_tracker)
        if(stepTrackerRunning) {
            if(stepTrackerBound) {
                unbindST()
                readout("Unbound.")
                bindButton.text = getString(R.string.bind_step_tracker)
            } else {
                bindST()
                readout("Binding . . .")
                bindButton.text = getString(R.string.unbind_step_tracker)
            }
        }
    }



}