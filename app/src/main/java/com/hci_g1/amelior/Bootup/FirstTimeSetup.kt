package com.hci_g1.amelior

import android.animation.ObjectAnimator
import android.content.pm.PackageManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.hardware.SensorManager
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.*
import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.view.*

class FirstTimeSetup: AppCompatActivity()
{
	private lateinit var buttonNext: Button
	private lateinit var nameInputField: EditText
	private lateinit var nameForm: LinearLayout

	override fun onCreate(savedInstanceState: Bundle?)
	{
		super.onCreate(savedInstanceState)
		setContentView(R.layout.first_time_setup)


		/* Initialize widgets. */
		nameInputField = findViewById(R.id.nameInputField)
		nameForm = findViewById(R.id.nameForm)

		/* UI logic. */
		nameInputField.setOnKeyListener(
			View.OnKeyListener { v, key, event ->

				if (key == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP)
				{
					/* TODO: This is not a solution. It will not scale. */
					ObjectAnimator.ofFloat(nameForm, "translationY", -500f).apply {
						duration = 500  // milliseconds
						start()
					}

					true
				}

				false
			}
		)

//		buttonNext = findViewById(R.id.proceedButton)
//
//		/* UI logic. */
//		buttonNext.setOnClickListener {
//			startActivity(Intent(this, MainMenuActivity::class.java))
//		}
	}

	companion object
	{
		private const val TAG = "FirstTimeSetup"
	}
}
