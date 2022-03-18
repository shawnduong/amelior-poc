package com.hci_g1.amelior

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity: AppCompatActivity()
{
	override fun onCreate(savedInstanceState: Bundle?)
	{
		super.onCreate(savedInstanceState)
		setContentView(R.layout.main_activity)

		/* Find the GPS button and have it start the buttonGPS service. */
		val buttonGPS: Button = findViewById(R.id.buttonGPS)
		buttonGPS.setOnClickListener { startService(Intent(applicationContext, GPS::class.java)) }
	}
}
