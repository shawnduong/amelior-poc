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

class FirstTimeSetup: AppCompatActivity()
{
	private lateinit var buttonNext: Button

	override fun onCreate(savedInstanceState: Bundle?)
	{
		super.onCreate(savedInstanceState)
		setContentView(R.layout.first_time_setup)

		/* Initialize widgets. */
		buttonNext = findViewById(R.id.proceedButton)

		/* UI logic. */
		buttonNext.setOnClickListener {
			startActivity(Intent(this, MainMenuActivity::class.java))
		}
	}

	companion object
	{
		private const val TAG = "FirstTimeSetup"
	}
}
