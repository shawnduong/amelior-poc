package com.hci_g1.amelior

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class GoalScreenCustomCreation: AppCompatActivity()
{
	private lateinit var spinnerAmountInputFrequency: Spinner
	private lateinit var switchNumericalSwitch: Switch
	private lateinit var textViewGoalPrompt: TextView

	override fun onCreate(savedInstanceState: Bundle?)
	{
		super.onCreate(savedInstanceState)
		setContentView(R.layout.goal_screen_custom_creation)

		/* Initialize widgets. */
		spinnerAmountInputFrequency  = findViewById(R.id.amountInputFrequency)
		switchNumericalSwitch        = findViewById(R.id.numericalSwitch)
		textViewGoalPrompt           = findViewById(R.id.goalPrompt)

		/* UI logic. */

		/* Set the frequency. */
		ArrayAdapter.createFromResource(
			this, R.array.frequencies, android.R.layout.simple_spinner_item
		)
		.also { adapter ->
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
			spinnerAmountInputFrequency.adapter = adapter
		}

		/* If the numerical switch is toggled, display the numerical input field. */
		switchNumericalSwitch.setOnCheckedChangeListener { _, checked ->

			/* TODO */
			if (checked)
			{
				textViewGoalPrompt.text = "I want to do"
			}
			else
			{
				textViewGoalPrompt.text = "I want to"
			}
		}
	}

	companion object
	{
		private val TAG = "GoalScreenCustomCreation"
	}
}
