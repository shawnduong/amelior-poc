package com.hci_g1.amelior

import android.content.pm.PackageManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.util.Log
import android.widget.*
import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class SplashScreen: AppCompatActivity()
{
	/* Services. */
	private val LOCATION_REQUEST_CODE: Int = 34 // Foreground only permissions request code
	private var ServiceGps: Gps? = null

	private val ACTIVITY_RECOGNITION_REQUEST_CODE: Int = 93
	private var stepTrackerRunning: Boolean = false

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

	override fun onCreate(savedInstanceState: Bundle?)
	{
		super.onCreate(savedInstanceState)
		setContentView(R.layout.splash_screen)

		/* Wait 3000 milliseconds before moving to the next screen. */
		Handler().postDelayed(
			{
				/* TODO: Only do first time setup the first time. */
				startActivity(Intent(this, FirstTimeSetup::class.java))
				finish()
			},
			1000  // milliseconds
		)

		request_all_permissions()
	}

	override fun onStart()
	{
		super.onStart()

		/* TODO: Switch to starting the GPS service only, rather than binding. */
		/* Bind to the GPS service. */
		Intent(this, Gps::class.java).also { intent ->
			bindService(intent, ConnectionGps, Context.BIND_AUTO_CREATE)
		}

		Intent(this, StepTracker::class.java).also { intent ->
			// Try to start the Step Tracker.
			stepTrackerRunning = (startService(intent) != null)

			// Report a failure if it couldn't start.
			if(!stepTrackerRunning)
				Log.e(TAG, "Step Tracker service fail to start.")
		}
	}

	private fun request_location_permissions(): Boolean
	{
		/* Request LOCATION permissions only if they haven't been granted already. */
		val existingPermission = ActivityCompat.checkSelfPermission(
			this,
			Manifest.permission.ACCESS_FINE_LOCATION
		)

		if (existingPermission != PackageManager.PERMISSION_GRANTED)
		{
			ActivityCompat.requestPermissions(
				this,
				arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
				LOCATION_REQUEST_CODE
			)

			return true
		}

		Log.e(TAG, "Location permissions denied.")
		return false
	}

	private fun request_activity_recognition_permissions(): Boolean
	{
		/* Request ACTIVITY_RECOGNITION permissions iff they're not already granted */
		val existingPermission = ContextCompat.checkSelfPermission(
			this,
			Manifest.permission.ACTIVITY_RECOGNITION
		)

		if(existingPermission != PackageManager.PERMISSION_GRANTED)
		{
			ActivityCompat.requestPermissions(
				this,
				arrayOf(Manifest.permission.ACTIVITY_RECOGNITION),
				ACTIVITY_RECOGNITION_REQUEST_CODE
			)

			return true
		}

		Log.e(TAG, "Activity Recognition permissions denied.")
		return false
	}

	private fun request_all_permissions(): Boolean
	{
		var allPermissionsGranted: Boolean = true
		allPermissionsGranted = allPermissionsGranted && request_location_permissions()
		allPermissionsGranted = allPermissionsGranted && request_activity_recognition_permissions()

		if(!allPermissionsGranted)
			Log.e(TAG, "One or more permissions were denied.")

		return allPermissionsGranted
	}

	companion object
	{
		private const val TAG = "SplashScreen"
	}
}
