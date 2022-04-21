package com.hci_g1.amelior

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI

import com.google.android.material.bottomnavigation.BottomNavigationView

class Dashboard: AppCompatActivity()
{
	override fun onCreate(savedInstanceState: Bundle?)
	{
		super.onCreate(savedInstanceState)
		setContentView(R.layout.dashboard)

		/* Get the NavController from the navhost this activity contains so we
		   can plug in the navbar to it. */
		val navhost = supportFragmentManager.findFragmentById(R.id.navhost)
		val navController = navhost?.findNavController()
		val navbar = findViewById<BottomNavigationView>(R.id.navbar)

		if (navController != null)
		{
			NavigationUI.setupWithNavController(navbar, navController)
			Log.d(TAG, "Navigation bar successfully set up.")
		}
	}

	companion object
	{
		private val TAG = "Dashboard"
	}
}
