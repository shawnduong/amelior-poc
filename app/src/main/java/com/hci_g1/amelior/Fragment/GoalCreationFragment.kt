package com.hci_g1.amelior

import android.animation.ObjectAnimator
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
//import androidx.compose.ui.platform.LocalDensity
import androidx.fragment.app.Fragment

import com.hci_g1.amelior.entities.User

class GoalCreationFragment: Fragment()
{
	private var scale: Float = 0f
	private var screenHeight: Float = 0f
	private var userActionChoice: Int = -1

	private lateinit var userDao: UserDao

	private lateinit var buttonContinueButton: Button
	private lateinit var editTextAmountInputQuantity: EditText
	private lateinit var linearLayoutPromptActionContainer: LinearLayout
	private lateinit var linearLayoutPromptAmountContainer: LinearLayout
	private lateinit var numberPickerPickAction: NumberPicker
	private lateinit var spinnerAmountInputFrequency: Spinner
	private lateinit var spinnerAmountInputUnits: Spinner
	private lateinit var textViewAmountFrequency: TextView
	private lateinit var textViewGreeting: TextView
	private lateinit var textViewPromptAction: TextView
	private lateinit var textViewPromptGeneral: TextView

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?): View?
	{
		return inflater.inflate(R.layout.fragment_create, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?)
	{
		super.onViewCreated(view, savedInstanceState)

		val context = getContext()

		/* Initializing variables. */
		if (context != null)
		{
			userDao = UserDatabase.getInstance(context).userDao
			Log.d(TAG, "Database successfully loaded.")
		}

		scale = resources.displayMetrics.density 

		/* Initialize widgets. */
		buttonContinueButton                = view.findViewById(R.id.continueButton)
		editTextAmountInputQuantity         = view.findViewById(R.id.amountInputQuantity)
		linearLayoutPromptActionContainer   = view.findViewById(R.id.promptActionContainer)
		linearLayoutPromptAmountContainer   = view.findViewById(R.id.promptAmountContainer)
		numberPickerPickAction              = view.findViewById(R.id.pickAction)
		spinnerAmountInputFrequency         = view.findViewById(R.id.amountInputFrequency)
		spinnerAmountInputUnits             = view.findViewById(R.id.amountInputUnits)
		textViewAmountFrequency             = view.findViewById(R.id.amountFrequency)
		textViewGreeting                    = view.findViewById(R.id.greeting)
		textViewPromptAction                = view.findViewById(R.id.promptAction)
		textViewPromptGeneral               = view.findViewById(R.id.promptGeneral)

		/* Set the action picker bounds. */
		// Index              0       1      2                       3
		val actions = arrayOf("walk", "run", "do something else...", "bike")
		numberPickerPickAction.minValue = 0
		numberPickerPickAction.maxValue = actions.size - 1
		numberPickerPickAction.displayedValues = actions

		/* UI logic. */

		/* Set the frequency. */
		if (context != null)
		{
			ArrayAdapter.createFromResource(
				context, R.array.frequencies, android.R.layout.simple_spinner_item
			)
			.also { adapter ->
				adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
				spinnerAmountInputFrequency.adapter = adapter
			}
		}

		/* Get the username from the database and set the greeting text. */
		val user = userDao.get_user_now("user")
		textViewGreeting.text = "Hello, ${user.name}!"

		/* Fade in the first text view and move it up. */
		ObjectAnimator.ofFloat(textViewGreeting, "alpha", 1.00f).apply {
			duration = 500  // milliseconds
			start()
		}
		ObjectAnimator.ofFloat(textViewGreeting, "translationY", 0.0f).apply {
			duration = 1000  // milliseconds
			start()
		}

		/* Fade in the general question prompt and move it up. */
		Handler().postDelayed(
			{
				ObjectAnimator.ofFloat(textViewPromptGeneral, "alpha", 1.00f).apply {
					duration = 500  // milliseconds
					start()
				}
				ObjectAnimator.ofFloat(textViewPromptGeneral, "translationY", 0.0f).apply {
					duration = 1000  // milliseconds
					start()
				}
			},
			1500
		)

		/* Fade in the action prompt. */
		Handler().postDelayed(
			{
				ObjectAnimator.ofFloat(linearLayoutPromptActionContainer, "alpha", 1.00f).apply {
					duration = 500  // milliseconds
					start()
				}
			},
			3000
		)

		/* Lambda for the action picker updates the selected value upon change. */
		numberPickerPickAction.setOnValueChangedListener { _, _, selection ->

			/* Make the next input UI elements appear, but only do it once. */
			if (userActionChoice == -1)
			{
				ObjectAnimator.ofFloat(linearLayoutPromptAmountContainer, "alpha", 1.00f).apply {
					duration = 500  // milliseconds
					start()
				}

				Handler().postDelayed(
					{
						spinnerAmountInputUnits.visibility = View.VISIBLE
						ObjectAnimator.ofFloat(spinnerAmountInputUnits, "alpha", 1.00f).apply {
							duration = 500  // milliseconds
							start()
						}
					},
					250  // milliseconds
				)
				Handler().postDelayed(
					{
						ObjectAnimator.ofFloat(textViewAmountFrequency, "alpha", 1.00f).apply {
							duration = 500  // milliseconds
							start()
						}
					},
					500  // milliseconds
				)
				Handler().postDelayed(
					{
						spinnerAmountInputFrequency.visibility = View.VISIBLE
						ObjectAnimator.ofFloat(spinnerAmountInputFrequency, "alpha", 1.00f).apply {
							duration = 500  // milliseconds
							start()
						}
					},
					750  // milliseconds
				)
			}

			/* Save the new selection. */
			userActionChoice = selection
			Log.d(TAG, "User chose ${actions[userActionChoice]}.")

			if (context != null)
			{
				var units: Int = 0

				/* "run" */
				if (userActionChoice == 1)
				{
					units = R.array.units_array_run
				}
				/* "do something else..." */
				else if (userActionChoice == 2)
				{
					/* TODO: handle this. */
					units = R.array.units_array_run
				}
				/* "bike" */
				else if (userActionChoice == 3)
				{
					units = R.array.units_array_bike
				}
				/* Default is walk. */
				else
				{
					units = R.array.units_array_walk
				}

				/* Set the spinner units based on the selection. */
				ArrayAdapter.createFromResource(
					context, units, android.R.layout.simple_spinner_item
				)
				.also { adapter ->
					adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
					spinnerAmountInputUnits.adapter = adapter
				}
			}
		}

		/* Upon finishing typing in the name, clear the focus. */
		editTextAmountInputQuantity.setOnKeyListener(

			View.OnKeyListener { _, key, event ->

				if (key == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP)
				{
					/* Clear the focus on the form item. */
					editTextAmountInputQuantity.clearFocus()

					/* Make the continue button appear. */
					ObjectAnimator.ofFloat(buttonContinueButton, "alpha", 1.00f).apply {
						duration = 500  // milliseconds
						start()
					}
				}

				false
			}
		)

		/* Upon pressing the button, save the data. */
		buttonContinueButton.setOnClickListener {

			val action: String = actions[userActionChoice]

			val quantity: Int = editTextAmountInputQuantity.text.toString().toInt()

			val units: String = spinnerAmountInputUnits.getItemAtPosition(
					spinnerAmountInputUnits.getSelectedItemPosition()
			).toString()

			val frequency: String = spinnerAmountInputFrequency.getItemAtPosition(
					spinnerAmountInputFrequency.getSelectedItemPosition()
			).toString()

			Log.d(TAG, "${action} ${quantity} ${units} ${frequency}")
		}
	}

	companion object
	{
		private val TAG = "GoalCreationFragment"
	}
}
