package com.hci_g1.amelior

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.add
import androidx.fragment.app.commit
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.main_activity.*

class MainActivity: AppCompatActivity()
{

	/**Step Counter Global Variables**/
	private var sensorManager: SensorManager? = null
	private var running = false
	private var totalSteps = 0f
	private var previousTotalSteps = 0f

	/** LIFECYCLE CALLBACKS **/
	override fun onCreate(savedInstanceState: Bundle?)
	{
		// Recover UI state if this activity was killed
		// See https://developer.android.com/guide/components/activities/activity-lifecycle#instance-state
		super.onCreate(savedInstanceState)
		setContentView(R.layout.main_activity)

		// Host the StepDisplay fragment whenever we instantiate this activity
		if(savedInstanceState == null)
		{
			supportFragmentManager.commit {
				setReorderingAllowed(true)
				add<StepDisplay>(R.id.step_display_container_view)
			}
		}
	}

}

