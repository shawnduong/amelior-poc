package com.hci_g1.amelior

import androidx.lifecycle.LifecycleService
import android.app.Service
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.IBinder
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

import com.hci_g1.amelior.entities.StepCount
import kotlinx.coroutines.coroutineScope

/* NOTE: Services use the main thread by default
*   May need to start a thread here so we don't accidentally block UI operations
*   See https://developer.android.com/guide/components/services#CreatingAService
*/

class StepTracker : LifecycleService(), SensorEventListener {
    /** STEP TRACKER CONTROL **/
    private var sensorManager: SensorManager? = null
    private var sensorRunning: Boolean = false
    private var stepTrackerOk: Boolean = false
    private fun isOk(): Boolean = stepTrackerOk

    /** STEP TRACKER DATA **/
    private var totalSteps: Float = 0f
    private var stepBaseline: Float = 0f
    private var stepBaselineEstablished: Boolean = false
    //private var updateSteps : (Float) -> Unit = {}

    private lateinit var stepCountDao: StepCountDao
    private lateinit var todaysStepCount: StepCount
    private var today: Long = 0

    /** UTILITY **/
    private fun get_epoch_day(): Long
    {
        val millisecPerDay: Long = 1000*60*60*24
        return (System.currentTimeMillis()/millisecPerDay)
    }

    /** LIFECYCLE CALLBACKS **/
    override fun onCreate() {
        super.onCreate()
		
		// Init variables
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        stepCountDao = UserDatabase.getInstance(this).stepCountDao
		
		today = get_epoch_day()
		
		// Make sure our data is initialized in the User Database
        if(stepCountDao.step_count_exists(today))
		{
  			todaysStepCount = stepCountDao.get_step_count_now(today)
			Log.d("StepTracker", "Retrieved the step count for epoch day $today.")
		}
		else
		{
			todaysStepCount = StepCount(today, 0)
			stepCountDao.insert_step_count_now(todaysStepCount)
			Log.d("StepTracker", "Initialized the step count for epoch day $today.")
		}
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int
    {
        // Step Tracker Listener
        val stepSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        if (stepSensor == null)
        {
            Log.d("StepTracker", "could not find a Step Sensor")
            stepTrackerOk = false
        }
        else
        {
            Log.d("StepTracker", "found a Step Sensor")
            sensorManager?.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_FASTEST)
            sensorRunning = true
            stepTrackerOk = true
        }

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy()
    {
		Log.d("StepTracker", "Service Destroyed!")
        super.onDestroy()
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int)
    {
        // Must be implemented for SensorEventListener Interface.
    }

    override fun onSensorChanged(event: SensorEvent?)
    {
		Log.d("StepTracker", "Sensor Event Seen!")
		
        if (sensorRunning)
        {
            if (stepBaselineEstablished)
            {
				totalSteps = event!!.values[0] - stepBaseline
				//TODO: Use lifecycleService for coroutine scope
				lifecycleScope.launch {
					todaysStepCount = StepCount (today, (totalSteps as Int))
					stepCountDao.insert_step_count(todaysStepCount)
					Log.d("StepTracker", "Inserted in Database")
				}
            }
            else
            {
				stepBaseline = event!!.values[0]
				stepBaselineEstablished = true
            }

            Log.d("StepTracker", "$totalSteps")
        }
    }
}
