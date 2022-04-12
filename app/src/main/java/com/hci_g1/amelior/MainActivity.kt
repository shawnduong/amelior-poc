package com.hci_g1.amelior

import android.content.pm.PackageManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.hardware.SensorManager
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.*
import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity

class MainActivity: AppCompatActivity()
{
	/* Services. */
	private var ServiceGps: Gps? = null
	private var ServiceGpsSubscribed: Boolean = false

	private val ACTIVITY_RECOGNITION_REQUEST_CODE: Int = 93
	private var stepTrackerRunning: Boolean = false
	private var stepTrackerSubscribed: Boolean = false
	private fun onStepUpdate(s: Float): Unit {/* Copy the value of s to a local step count */}

	/* Widgets. */
	private lateinit var welcomeNextButton: Button

	/* GPS service connection. */
	private val ConnectionGps = object: ServiceConnection
	{
		override fun onServiceConnected(name: ComponentName, service: IBinder)
		{
			val binder = service as Gps._Binder
			ServiceGps = binder.get_service()
			Log.d(TAG, "GPS service connected.")
		}

		override fun onServiceDisconnected(name: ComponentName)
		{
			ServiceGps = null
			Log.d(TAG, "GPS service disconnected.")
		}
	}

	/* Step Tracker service connection */
	private val ConnStepTracker = object : ServiceConnection
	{
		override fun onServiceConnected(name: ComponentName, service: IBinder)
		{
			val binding = service as StepTracker.STBinding
			binding.injectOnStepUpdate(::onStepUpdate)
			stepTrackerSubscribed = true
			Log.d(TAG,"Step Tracker service connected.")
		}

		override fun onServiceDisconnected(name: ComponentName)
		{
			stepTrackerSubscribed = false
			Log.d(TAG,"Step Tracker service was killed or disconnected.")
		}
	}

	override fun onCreate(savedInstanceState: Bundle?)
	{
		super.onCreate(savedInstanceState)
		setContentView(R.layout.main_activity)

		/* Initialize widgets. */
		welcomeNextButton = findViewById(R.id.welcomeNextButton)

		/* UI logic. */
		welcomeNextButton.setOnClickListener {
			val intent = Intent(this, QuestionActivity::class.java)
			startActivity(intent)
		}

		request_permissions()
	}

	override fun onStart()
	{
		super.onStart()

		/* Bind to the GPS service. */
		Intent(this, Gps::class.java).also { intent ->
			bindService(intent, ConnectionGps, Context.BIND_AUTO_CREATE)
		}

		Intent(this, StepTracker::class.java).also { intent ->  
			stepTrackerRunning = (startService(intent) != null)

			if(stepTrackerRunning)
				bindService(intent, ConnStepTracker, Context.BIND_AUTO_CREATE)
			else
				Log.e(TAG, "Step Tracker service fail to start.")
		}
	}

	private fun request_permissions(): Boolean
	{
		/* Request LOCATION permissions only if they haven't been granted already. */
		if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
			!= PackageManager.PERMISSION_GRANTED)
		{
			ActivityCompat.requestPermissions(
				this,
				arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
				34  /* Foreground only permissions request code */
			)

			//return true
		}

		/* Request ACTIVITY_RECOGNITION permissions iff they're not already granted */
		val permissionAR = ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION)
		if(permissionAR != PackageManager.PERMISSION_GRANTED)
		{
			ActivityCompat.requestPermissions(
				this,
				arrayOf(Manifest.permission.ACTIVITY_RECOGNITION),
				ACTIVITY_RECOGNITION_REQUEST_CODE
			)
		}

		//TODO(reason =
		// Move each permission block to dedicated routines,
		// Call all permission routines from an aggregation method)
		//return true

		return false
	}

	companion object
	{
		private const val TAG = "MainActivity"
	}
}
