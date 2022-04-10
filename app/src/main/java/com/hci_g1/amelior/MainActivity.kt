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

class MainActivity: AppCompatActivity()
{
	/* Services. */
	private var ServiceGps: Gps? = null
	private var ServiceGpsSubscribed: Boolean = false

	/* TODO: Handle these when refactoring step counter.
	 * "Those variables don't need to be there because they're in StepTracker.kt"
	 * - Charison
	 */
	/**Step Counter Global Variables**/
	private var sensorManager: SensorManager? = null
	private var running = false
	private var totalSteps = 0f
	private var previousTotalSteps = 0f

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

			return true
		}

		return false
	}

	companion object
	{
		private const val TAG = "MainActivity"
	}
}
