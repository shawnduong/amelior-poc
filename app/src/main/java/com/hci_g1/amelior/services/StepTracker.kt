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
//import androidx.lifecycle.lifecycleScope	// Use livedata instead! Services dont have a views!
import kotlinx.coroutines.launch

import com.hci_g1.amelior.entities.StepCount
import kotlinx.coroutines.coroutineScope

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
    //private var updateSteps : (Float) -> Unit = {}

    private lateinit var stepCountDao: StepCountDao
    private lateinit var todaysStepCount: StepCount
    private var today: Long = 0

    /** INTERFACE **/
//    inner class STBinding : Binder()
//    {
//        fun isOk() : Boolean { return this@StepTracker.isOk() }
//        fun injectOnStepUpdate(onStepUpdate: (Float)->Unit) : Unit { updateSteps = onStepUpdate }
//    }

    // Instantiate the interface to return it to clients
//    private val binding = STBinding()

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

        // TODO(reason = Cleanup secondary permission check)
        // Runtime Permissions
//        val permission = ContextCompat.checkSelfPermission(this,
//            Manifest.permission.ACTIVITY_RECOGNITION)
//        if(permission == PackageManager.PERMISSION_DENIED) {
//            stepTrackerOk = false
//            Log.d("StepTracker", "got PERMISSION DENIED")
//        } else {
//            stepTrackerOk = true
//        }
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
        super.onDestroy()
    }

    /** BINDING CALLBACKS **/
    override fun onBind(intent: Intent?): IBinder?
    {
        return null
    }

    override fun onUnbind(intent: Intent?): Boolean
    {
        //TODO(reason = Test a reset of the injected callback on unbind/rebind)
        //updateSteps = {} // Eject the callback that was injected from calling bind().
        return true
    }

    override fun onRebind(intent: Intent?)
    {
        super.onRebind(intent)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int)
    {
        // Must be implemented for SensorEventListener Interface.
    }

    override fun onSensorChanged(event: SensorEvent?)
    {
        if (sensorRunning)
        {
            if (stepBaselineEstablished)
            {
				totalSteps = event!!.values[0] - stepBaseline
				//TODO: Use livedata for coroutine scope
//				todaysStepCount = StepCount (today, (totalSteps as Int))
//				stepCountDao.insert_step_count(todaysStepCount)
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
