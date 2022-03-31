package com.hci_g1.amelior

import android.app.Service
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Binder
import android.os.IBinder
import android.util.Log
import android.widget.Toast

/* NOTE: Services use the main thread by default
*   May need to start a thread here so we don't accidentally block UI operations
*   See https://developer.android.com/guide/components/services#CreatingAService
*/

class StepTracker : Service(), SensorEventListener
{
    /**SENSOR VARIABLES**/
    private var sensorManager: SensorManager? = null
    private var running = false
    private var totalSteps = 0f
    private var previousTotalSteps = 0f
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

        loadData()
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)

        status += "Started StepTracker (request: $startId)\n"

        TODO(reason = "Implement safe start-ups")
        /*
            flags and startId can be used to gracefully handle
            our service getting killed while starting.
            Come back around after main functionality is done.
         */
        /**SENSOR SETUP**/
        val stepSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        if (stepSensor == null) {
            Toast.makeText(this, "No sensor detected on this device", Toast.LENGTH_SHORT).show()
        } else {
            sensorManager?.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI)
        }

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

    /**SENSOR CALLBACKS**/
    override fun onSensorChanged(event: SensorEvent?) {
//        var tv_stepsTaken = findViewById<TextView>(R.id.tv_stepsTaken)
        if (running) {
            totalSteps = event!!.values[0]
            val currentSteps: Int = totalSteps.toInt() - previousTotalSteps.toInt()
            status = ("$currentSteps")
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    // Must be implemented for SensorEventListener
    }
//
//    private fun saveData() {
//        val sharedPreferences = getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
//        val editor = sharedPreferences.edit()
//        editor.putFloat("key1", previousTotalSteps)
//        editor.apply()
//    }

    private fun loadData() {
        val sharedPreferences = getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
        val savedNumber = sharedPreferences.getFloat("key1", 0f)
        Log.d("MainActivity", "$savedNumber")
        previousTotalSteps = savedNumber
    }
}