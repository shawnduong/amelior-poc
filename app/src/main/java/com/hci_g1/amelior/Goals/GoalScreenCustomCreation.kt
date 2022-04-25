package com.hci_g1.amelior

import android.animation.ObjectAnimator
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class GoalScreenCustomCreation: AppCompatActivity()
{
	private lateinit var relativeLayoutBasicGoalContainer: RelativeLayout
	private lateinit var relativeLayoutNumericalGoalContainer: RelativeLayout
	private lateinit var spinnerAmountInputFrequency: Spinner
	private lateinit var switchNumericalSwitch: Switch
	private lateinit var textViewGoalPrompt: TextView

	override fun onCreate(savedInstanceState: Bundle?)
	{
		super.onCreate(savedInstanceState)
		setContentView(R.layout.goal_screen_custom_creation)

		/* Initialize widgets. */
		relativeLayoutBasicGoalContainer      = findViewById(R.id.basicGoalContainer)
		relativeLayoutNumericalGoalContainer  = findViewById(R.id.numericalGoalContainer)
		spinnerAmountInputFrequency           = findViewById(R.id.amountInputFrequency)
		switchNumericalSwitch                 = findViewById(R.id.numericalSwitch)
		textViewGoalPrompt                    = findViewById(R.id.goalPrompt)

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

			if (checked)
			{
				/* Fade in the numerical goal UI. */
				relativeLayoutNumericalGoalContainer.visibility = View.VISIBLE
				ObjectAnimator.ofFloat(relativeLayoutNumericalGoalContainer, "alpha", 1.00f).apply {
					duration = 500  // milliseconds
					start()
				}

				/* Fade out the basic goal UI. */
				ObjectAnimator.ofFloat(relativeLayoutBasicGoalContainer, "alpha", 0.00f).apply {
					duration = 500  // milliseconds
					start()
				}
				Handler().postDelayed(
					{
						relativeLayoutBasicGoalContainer.visibility = View.INVISIBLE
					},
					500
				)
			}
			else
			{
				/* Fade in the basic goal UI. */
				relativeLayoutBasicGoalContainer.visibility = View.VISIBLE
				ObjectAnimator.ofFloat(relativeLayoutBasicGoalContainer, "alpha", 1.00f).apply {
					duration = 500  // milliseconds
					start()
				}

				/* Fade out the numerical goal UI. */
				ObjectAnimator.ofFloat(relativeLayoutNumericalGoalContainer, "alpha", 0.00f).apply {
					duration = 500  // milliseconds
					start()
				}
				Handler().postDelayed(
					{
						relativeLayoutNumericalGoalContainer.visibility = View.INVISIBLE
					},
					500
				)
			}
		}
	}

	companion object
	{
		private val TAG = "GoalScreenCustomCreation"
	}
}
