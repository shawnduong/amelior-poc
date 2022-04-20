package com.hci_g1.amelior

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.util.Log
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

import com.hci_g1.amelior.entities.User

class SplashScreen: AppCompatActivity()
{
	/* Services. */
	private var ServiceGps: Gps? = null

	private var stepTrackerRunning: Boolean = false

	private lateinit var userDao: UserDao

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

		/* Initialize variables. */
		userDao = UserDatabase.getInstance(this).userDao

		/* Wait 1000 milliseconds before moving to the next screen. */
		Handler().postDelayed(
			{
				/* Force non-fullscreen to prevent activity flickering. */
				window.setFlags(
					WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN,
					WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN
				)

				if (userDao.is_setup() == false)
				{
					Log.d(TAG, "Moving to FirstTimeSetup.")
					startActivity(Intent(this, FirstTimeSetup::class.java))
				}
				else
				{
					Log.d(TAG, "Moving to Dashboard.")
					startActivity(Intent(this, Dashboard::class.java))
				}

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

	companion object
	{
		private const val TAG = "SplashScreen"
	}
}
