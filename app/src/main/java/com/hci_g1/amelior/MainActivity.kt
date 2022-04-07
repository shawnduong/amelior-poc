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
import androidx.lifecycle.lifecycleScope
import com.hci_g1.amelior.entities.relations.UserHealthCrossRef
import com.hci_g1.amelior.entities.User
import com.hci_g1.amelior.entities.Health
import kotlinx.coroutines.launch

class MainActivity: AppCompatActivity()
{
	private var ServiceGPS: GPS? = null
	private var ServiceGPSSubscribed: Boolean = false

	private lateinit var buttonGPS: Button

	private val ConnectionGPS = object: ServiceConnection
	{
		override fun onServiceConnected(name: ComponentName, service: IBinder)
		{
			val binder = service as GPS._Binder
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
		buttonGPS.setOnClickListener {
			if (ServiceGPS != null)
			{
				if ((ServiceGPSSubscribed == false) && (ServiceGPS?.subscribe() == true))
				{
					ServiceGPSSubscribed = true
					buttonGPS.text = "Disable GPS."
				}
				else if ((ServiceGPSSubscribed == true) && (ServiceGPS?.unsubscribe() == true))
				{
					ServiceGPSSubscribed = false
					buttonGPS.text = "Enable GPS."
				}
			}
		}

    val dao = UserDatabase.getInstance(this).userDao

		val healths = listOf (
			Health(1, 300, 400, 120.27),
			Health(2, 400, 200, 120.73)
		)

		val users = listOf (
			User("Testing1", 18,"F", 120.7, 160.3),
			User("Testing2", 20,"F", 115.0, 170.5)
		)

		val userHealthRelations = listOf (
			UserHealthCrossRef("Testing1", 1),
			UserHealthCrossRef("Testing2", 2)
		)

		lifecycleScope.launch {
			healths.forEach { dao.insertHealth(it) }
			users.forEach { dao.insertUser(it) }
			userHealthRelations.forEach { dao.insertUserHealthCrossRef(it) }
		}
    
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
