package com.hci_g1.amelior

import android.Manifest
import android.app.Activity
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Binder
import android.os.IBinder
import android.util.Log
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat

/* NOTE: Services use the main thread by default
*   May need to start a thread here so we don't accidentally block UI operations
*   See https://developer.android.com/guide/components/services#CreatingAService
*/

class StepTracker : Service(), SensorEventListener {
    /** STEP SENSOR **/
    private var sensorManager: SensorManager? = null
    private var sensorRunning: Boolean = false

    /** DATA **/
    private var status: String = "Lorem Ipsum"
    private var demand: Int = 0
    private var totalSteps: Float = 0f
    private var previousTotalSteps: Float = 0f

    private fun getStatus(): String
    {
        return status
    }

    /** INTERFACE **/
    // Provide the status string to clients
    private lateinit var readSteps : (Float) -> Float

    inner class STBinding : Binder()
    {
        fun getStepTrackerStatus() : String { return this@StepTracker.getStatus() }
        fun stepTrackerCallback(cb: (Float)->Float) : Unit {
            readSteps = cb
            return
        }
    }

    // Instantiate the interface to return it to clients
    private val binding = STBinding()

    /** LIFECYCLE CALLBACKS **/
    override fun onCreate() {
        super.onCreate()
        status = "Created new StepTracker\n"

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        // Runtime Permissions
        val permission = ContextCompat.checkSelfPermission(this,
            Manifest.permission.ACTIVITY_RECOGNITION)
        if(permission == PackageManager.PERMISSION_DENIED) {
            //requestPermissions(getActivity(), arrayOf(Manifest.permission.ACTIVITY_RECOGNITION), 1)
            //TODO(reason = "Improve Permission Denial Handling")
            Log.d("StepTracker", "PERMISSION DENIED!!!")
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        status += "Started StepTracker (request: $startId)\n"

        // Step Tracker Listener
        val stepSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        if (stepSensor == null) {
            status = "No Sensor"
        } else {
            sensorManager?.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI)
            sensorRunning = true
        }

        return super.onStartCommand(intent, flags, startId)
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
        status += "Client Unbound\nNow bound to $demand clients\n"

        //return super.onUnbind(intent)
        return true
    }

    override fun onRebind(intent: Intent?) {
        demand++
        status += "Client Rebound\nNow bound to $demand clients\n"

        super.onRebind(intent)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Must be implemented for SensorEventListener Interface.
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (sensorRunning) {
            totalSteps = event!!.values[0]
            readSteps(totalSteps)
            Log.d("StepTracker", "Sensor heard an event!")
            //val currentSteps: Int = totalSteps.toInt() - previousTotalSteps.toInt()
        }
    }
}