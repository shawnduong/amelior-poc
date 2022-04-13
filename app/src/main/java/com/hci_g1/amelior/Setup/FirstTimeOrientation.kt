package com.hci_g1.amelior

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class FirstTimeOrientation: AppCompatActivity()
{
	override fun onCreate(savedInstanceState: Bundle?)
	{
		super.onCreate(savedInstanceState)
		setContentView(R.layout.first_time_orientation)
	}

	companion object
	{
		private const val TAG = "FirstTimeOrientation"
	}
}
