package com.hci_g1.amelior

import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log

import com.hci_g1.amelior.entities.StepCount

/* NOTE: Services use the main thread by default
*   May need to start a thread here so we don't accidentally block UI operations
*   See https://developer.android.com/guide/components/services#CreatingAService
*/

// TODO: Add stopSelf() calls when we fail to acquire the step sensor
class StepTracker : LifecycleService(), SensorEventListener {
    /** STEP TRACKER CONTROL **/
    private var sensorManager: SensorManager? = null
	private var stepSensorIsRegistered: Boolean = false
    private var stepTrackerIsRunning: Boolean = false

    /** STEP SENSOR DATA VARS **/
	private var savedSteps: Float = 0f
    private var stepBaseline: Float = 0f
    private var stepBaselineEstablished: Boolean = false
    private var totalSteps: Float = 0f
	
	/** DATABASE INTERACTION **/
    private lateinit var stepCountDao: StepCountDao
    private lateinit var todaysStepCount: StepCount
    private var today: Long = 0
	
    private fun get_epoch_day(): Long
    {
        val millisecPerDay: Long = 1000*60*60*24
        return (System.currentTimeMillis()/millisecPerDay)
    }

    /** LIFECYCLE CALLBACKS **/
    override fun onCreate() {
        super.onCreate()
		Log.d(TAG, "Created the Step Tracker service.")
		
		/* Init Objects and Variables */
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
		
        stepCountDao = UserDatabase.getInstance(this).stepCountDao
		today = get_epoch_day()
		
		// Make sure our data is initialized in the User Database
        if(stepCountDao.step_count_exists(today))
		{
  			todaysStepCount = stepCountDao.get_step_count_now(today)
			savedSteps = todaysStepCount.stepTotal
			Log.d(TAG, "Retrieved ${todaysStepCount.stepTotal} saved steps for epoch day $today.")
		}
		else
		{
			todaysStepCount = StepCount(today, 0f)
			stepCountDao.insert_step_count_now(todaysStepCount)
			Log.d(TAG, "Initialized the step count for epoch day $today.")
		}
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int
    {
		Log.d(TAG, "Starting the Step Tracker service.")
		
		// Step Tracker Listener
        val stepSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        if (stepSensor == null)
        {
            Log.e(TAG, "Failed to find a Step Sensor on this device.")
        }
        else
        {
            Log.d(TAG, "Successfully found a Step Sensor on this device.")
			
            stepSensorIsRegistered = sensorManager!!.registerListener(
				this,
				stepSensor,
				SensorManager.SENSOR_DELAY_FASTEST
			)
			
			if(stepSensorIsRegistered)
			{
				Log.d(TAG, "Step Sensor successfully registered as an event listener.")
            	stepTrackerIsRunning = true
			}
			else
			{
				Log.e(TAG, "Step Sensor failed to register as an event listener.")
			}
        }

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy()
    {
		Log.d(TAG, "Destroyed the Step Tracker service.")
        super.onDestroy()
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int)
    {
        // Must be implemented for SensorEventListener Interface.
    }

    override fun onSensorChanged(event: SensorEvent?)
    {
		Log.d(TAG, "Step Sensor detected an event.")
		
        if (stepTrackerIsRunning)
        {
            if (stepBaselineEstablished)
            {
				totalSteps = event!!.values[0] - stepBaseline + savedSteps
				
				lifecycleScope.launch {
					todaysStepCount = StepCount (today, totalSteps)
					stepCountDao.insert_step_count(todaysStepCount)
					Log.d(TAG, "Updated database with ${todaysStepCount.stepTotal} steps for epoch day $today.")
				}
            }
            else
            {
				stepBaseline = event!!.values[0]
				stepBaselineEstablished = true
            }

            Log.d(TAG, "Step Tracker has detected $totalSteps steps for epoch day $today.")
        }
    }
	
	companion object
	{
		/* Logging tag. */
		private const val TAG = "StepTracker"
	}
}
