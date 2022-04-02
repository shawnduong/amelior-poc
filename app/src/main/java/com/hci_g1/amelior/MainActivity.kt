package com.hci_g1.amelior

import android.content.pm.PackageManager
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

class MainActivity: AppCompatActivity()
{
	override fun onCreate(savedInstanceState: Bundle?)
	{
		super.onCreate(savedInstanceState)
		setContentView(R.layout.main_activity)

		request_permissions()
	}

	/* Request permissions. */
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
}
