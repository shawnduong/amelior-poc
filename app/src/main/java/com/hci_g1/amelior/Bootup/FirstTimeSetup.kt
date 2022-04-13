package com.hci_g1.amelior

import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.KeyEvent
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class FirstTimeSetup: AppCompatActivity()
{
	private lateinit var linearLayoutNameForm: LinearLayout
	private lateinit var editTextNameInputField: EditText

	override fun onCreate(savedInstanceState: Bundle?)
	{
		super.onCreate(savedInstanceState)
		setContentView(R.layout.first_time_setup)


		/* Initialize widgets. */
		linearLayoutNameForm = findViewById(R.id.nameForm)
		editTextNameInputField = findViewById(R.id.nameInputField)

		/* UI logic. */

		/* Upon finishing typing in the name, move the form field up. */
		editTextNameInputField.setOnKeyListener(

			View.OnKeyListener { v, key, event ->

				if (key == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP)
				{
					val displayMetrics = DisplayMetrics()
					windowManager.defaultDisplay.getMetrics(displayMetrics)

					val height = displayMetrics.heightPixels.toFloat()

					ObjectAnimator.ofFloat(linearLayoutNameForm, "translationY", height * -0.33f).apply {
						duration = 500  // milliseconds
						start()
					}

					ObjectAnimator.ofFloat(linearLayoutNameForm, "alpha", 0.25f).apply {
						duration = 500  // milliseconds
						start()
					}

					true
				}

				false
			}
		)
	}

	companion object
	{
		private const val TAG = "FirstTimeSetup"
	}
}
