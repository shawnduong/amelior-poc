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
	private var ServiceGps: Gps? = null
	private val LOCATION_REQUEST_CODE: Int = 10

	private var stepTrackerRunning: Boolean = false
	private val ACTIVITY_RECOGNITION_REQUEST_CODE: Int = 20

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

		/* TODO: We should actually do this at orientation and in the settings. */
		/* Request all permissions before moving to the next activity. */
		request_all_permissions()

		/* Wait 1000 milliseconds before moving to the next screen. */
		Handler().postDelayed(
			{
				/* TODO: Only do first time setup the first time. */
				Log.d(TAG, "Moving to FirstTimeSetup.")
				startActivity(Intent(this, FirstTimeSetup::class.java))
				finish()
			},
			1000  // milliseconds
		)
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

	/* Request permissions and return true if the user either already has it or
	   if the user accepts the permissions request prompt. Else, return false. */
	private fun request_permissions(description: String, permission: String, code: Int): Boolean
	{
		/* Return true if permissions have already been granted. */
		if (has_permissions(permission))
		{
			Log.d(TAG, "User has already accepted ${description} permissions.")
			return true
		}
		/* Ask the user for permissions. */
		else
		{
			Log.d(TAG, "User has not accepted ${description} permissions. Asking now.")

			ActivityCompat.requestPermissions(
				this,
				arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
				code
			)

			if (has_permissions(permission) == false)
			{
				Log.d(TAG, "User denied ${description} permissions.")
				return false
			}
			else
			{
				return true
			}
		}
	}

	/* Returns true if the permission is granted already. */
	private fun has_permissions(permission: String): Boolean
	{
		return (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED)
	}

	/* Request all permissions and return true if all permissions are satisfied. */
	private fun request_all_permissions(): Boolean
	{
		var allPermissionsGranted: Boolean = true

		allPermissionsGranted = allPermissionsGranted && request_permissions(
			"location",
			Manifest.permission.ACCESS_FINE_LOCATION,
			LOCATION_REQUEST_CODE
		)

		allPermissionsGranted = allPermissionsGranted && request_permissions(
			"activity recognition",
			Manifest.permission.ACTIVITY_RECOGNITION,
			ACTIVITY_RECOGNITION_REQUEST_CODE
		)

		if (allPermissionsGranted == true)
		{
			Log.e(TAG, "All permissions are accepted.")
			return true
		}

		Log.e(TAG, "One or more permissions were denied.")
		return false
	}

	companion object
	{
		private const val TAG = "SplashScreen"
	}
}
