package com.hci_g1.amelior

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

import com.hci_g1.amelior.entities.Goal

class GoalScreenCustomCreation: AppCompatActivity()
{
	private var isNumerical: Boolean = false

	private lateinit var goalDao: GoalDao

	private lateinit var buttonCreateButton: Button
	private lateinit var editTextGoalInputField: EditText
	private lateinit var editTextNumericalGoalInputField: EditText
	private lateinit var editTextNumericalGoalInputQuantity: EditText
	private lateinit var relativeLayoutBasicGoalContainer: RelativeLayout
	private lateinit var relativeLayoutNumericalGoalContainer: RelativeLayout
	private lateinit var spinnerAmountInputFrequency: Spinner
	private lateinit var switchNumericalSwitch: Switch
	private lateinit var textViewGoalPrompt: TextView

	override fun onCreate(savedInstanceState: Bundle?)
	{
		super.onCreate(savedInstanceState)
		setContentView(R.layout.goal_screen_custom_creation)

		/* Initialize variables. */
		goalDao = UserDatabase.getInstance(this).goalDao

		/* Initialize widgets. */
		buttonCreateButton                    = findViewById(R.id.createButton)
		editTextGoalInputField                = findViewById(R.id.goalInputField)
		editTextNumericalGoalInputField       = findViewById(R.id.numericalGoalInputField)
		editTextNumericalGoalInputQuantity    = findViewById(R.id.numericalGoalInputQuantity)
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
				isNumerical = true

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
				isNumerical = false

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

		/* Upon pressing the create button, save the data and go home. */
		buttonCreateButton.setOnClickListener {

			var action: String? = null
			var quantity: Int? = null
			var units: String? = null
			var frequency: String? = null

			if (isNumerical)
			{
				action = "do"
				quantity = editTextNumericalGoalInputQuantity.text.toString().toInt()
				units = editTextNumericalGoalInputField.text.toString()
			}
			else
			{
				action = editTextGoalInputField.text.toString()
				quantity = -1
				units = "N/A"
			}

			frequency = spinnerAmountInputFrequency.getItemAtPosition(
				spinnerAmountInputFrequency.getSelectedItemPosition()
			).toString()

			goalDao.insert_goal_now(
				Goal(
					goalDao.size(),  // key
					true,            // custom
					action,          // action
					quantity,        // quantity
					units,           // units
					frequency,       // frequency
					3,               // level
					-1               // last completed
				)
			)

			/* Test that it was inserted correctly. */
			val goal: Goal = goalDao.get_goal_now(goalDao.size()-1)
			Log.d(TAG, "User created goal ${goal.key} ${goal.action} ${goal.quantity} ${goal.units} ${goal.frequency}")

			/* Go home. */
			startActivity(Intent(this, Dashboard::class.java))
			finish()
		}
	}

	companion object
	{
		private val TAG = "GoalScreenCustomCreation"
	}
}
