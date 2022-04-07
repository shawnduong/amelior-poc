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
    /** STEP TRACKER CONTROL **/
    private var sensorManager: SensorManager? = null
    private var sensorRunning: Boolean = false
    private var stepTrackerOk: Boolean = false
    private fun isOk(): Boolean = stepTrackerOk

    /** STEP TRACKER DATA **/
    private var totalSteps: Float = 0f
    private var stepBaseline: Float = 0f
    private var stepBaselineEstablished: Boolean = false

    /** INTERFACE **/
    private var readSteps : (Float) -> Float = {0f}

    inner class STBinding : Binder()
    {
        fun isOk() : Boolean { return this@StepTracker.isOk() }
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
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        // Runtime Permissions
        val permission = ContextCompat.checkSelfPermission(this,
            Manifest.permission.ACTIVITY_RECOGNITION)
        if(permission == PackageManager.PERMISSION_DENIED) {
            stepTrackerOk = false
            Log.d("StepTracker", "got PERMISSION DENIED")
        } else {
            stepTrackerOk = true
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Step Tracker Listener
        val stepSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        if (stepSensor == null) {
            Log.d("StepTracker", "could not find a Step Sensor")
            stepTrackerOk = false
        } else {
            Log.d("StepTracker", "found a Step Sensor")
            sensorManager?.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_FASTEST)
            sensorRunning = true
            stepTrackerOk = stepTrackerOk && true
        }

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    /** BINDING CALLBACKS **/
    override fun onBind(intent: Intent?): IBinder? {
        return binding
    }

    override fun onUnbind(intent: Intent?): Boolean {
        return true
    }

    override fun onRebind(intent: Intent?) {
        super.onRebind(intent)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Must be implemented for SensorEventListener Interface.
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (sensorRunning) {
            if (!stepBaselineEstablished) {
                stepBaseline = event!!.values[0]
                stepBaselineEstablished = true
            }
            else{
                totalSteps = event!!.values[0] - stepBaseline
                readSteps(totalSteps)
            }

            Log.d("StepTracker", "$totalSteps")
        }
    }
}