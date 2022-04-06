package com.hci_g1.amelior

import android.content.pm.PackageManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

class MainActivity: AppCompatActivity()
{
	private var ServiceGPS: GPS? = null

	private lateinit var buttonGPS: Button

	private val ConnectionGPS = object: ServiceConnection
	{
		override fun onServiceConnected(name: ComponentName, service: IBinder)
		{
			val binder = service as GPS.LocalBinder
			ServiceGPS = binder.get_service()
			Log.d(TAG, "Service connected.")
		}

		override fun onServiceDisconnected(name: ComponentName)
		{
			ServiceGPS = null
			Log.d(TAG, "Service disconnected.")
		}
	}

	override fun onCreate(savedInstanceState: Bundle?)
	{
		super.onCreate(savedInstanceState)
		setContentView(R.layout.main_activity)

		/* Initialize widgets. */
		buttonGPS = findViewById(R.id.buttonGPS)

		/* UI logic. */
		buttonGPS.setOnClickListener { if (ServiceGPS != null) ServiceGPS?.subscribe() }

		request_permissions()
	}

	override fun onStart()
	{
		super.onStart()

		/* Bind to the GPS service. */
		Intent(this, GPS::class.java).also { intent ->
			bindService(intent, ConnectionGPS, Context.BIND_AUTO_CREATE)
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
