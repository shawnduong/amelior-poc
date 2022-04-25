package com.hci_g1.amelior

import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

import com.hci_g1.amelior.entities.Goal

class GoalScreenBasic: AppCompatActivity()
{
	private lateinit var goal: Goal

	private lateinit var imageViewFlowerGraphic: ImageView
	private lateinit var textViewTitle: TextView
	private lateinit var textViewSubtitle: TextView

	override fun onCreate(savedInstanceState: Bundle?)
	{
		super.onCreate(savedInstanceState)
		setContentView(R.layout.goal_screen_basic)

		/* Reading the passed in initCode; this is the goal's DB row (key). */
		val initCode: Int? = getIntent().getIntExtra("initIndex", 0)
		Log.d(TAG, "Loaded with initCode=${initCode}.")

		/* Initialize variables. */
		if (initCode != null)
		{
			val goalDao = UserDatabase.getInstance(this).goalDao
			goal = goalDao.get_goal_now(initCode)
			Log.d(TAG, "Goal successfully loaded.")
		}

		/* Initialize widgets. */
		imageViewFlowerGraphic  = findViewById(R.id.flowerGraphic)
		textViewTitle           = findViewById(R.id.title)
		textViewSubtitle        = findViewById(R.id.subtitle)

		/* Fill in the data. */
		textViewTitle.text = "${goal.action.capitalize()} ${goal.quantity} ${goal.units}"
		textViewSubtitle.text = "every ${goal.frequency}"

		/* Set the image based on the level. */
		if (goal.level == 1)
		{
			imageViewFlowerGraphic.setImageResource(R.drawable.flower_level_01)
		}
		else if (goal.level == 2)
		{
			imageViewFlowerGraphic.setImageResource(R.drawable.flower_level_02)
		}
		else if (goal.level == 3)
		{
			imageViewFlowerGraphic.setImageResource(R.drawable.flower_level_03)
		}
		else if (goal.level == 4)
		{
			imageViewFlowerGraphic.setImageResource(R.drawable.flower_level_04)
		}
		else if (goal.level == 5)
		{
			imageViewFlowerGraphic.setImageResource(R.drawable.flower_level_05)
		}
		/* 3 is the default level. */
		else
		{
			imageViewFlowerGraphic.setImageResource(R.drawable.flower_level_03)
		}
	}

	companion object
	{
		private val TAG = "GoalScreenBasic"
	}
}
