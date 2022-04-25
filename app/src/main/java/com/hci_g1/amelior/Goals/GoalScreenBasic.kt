package com.hci_g1.amelior

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

import com.hci_g1.amelior.entities.Goal

class GoalScreenBasic: AppCompatActivity()
{
	private lateinit var goal: Goal

	override fun onCreate(savedInstanceState: Bundle?)
	{
		super.onCreate(savedInstanceState)
		setContentView(R.layout.goal_screen_basic)

		/* Reading the passed in initCode; this is the goal's DB row (key). */
		val initCode: Int? = getIntent().getIntExtra("initIndex", 0)
		Log.d(TAG, "Loaded with initCode=${initCode}.")

		/* Initializing variables. */
		if (initCode != null)
		{
			val goalDao = UserDatabase.getInstance(this).goalDao
			goal = goalDao.get_goal_now(initCode)
			Log.d(TAG, "Goal successfully loaded.")
			Log.d(TAG, "${goal.action} ${goal.quantity} ${goal.units} ${goal.frequency}")
		}
	}

	companion object
	{
		private val TAG = "GoalScreenBasic"
	}
}
