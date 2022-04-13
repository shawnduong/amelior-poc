package com.hci_g1.amelior

import android.animation.ObjectAnimator
import android.os.Bundle
import android.os.Handler
import android.util.DisplayMetrics
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class FirstTimeSetup: AppCompatActivity()
{
	private lateinit var editTextNameInputField: EditText
	private lateinit var imageViewMoodGraphic: ImageView
	private lateinit var linearLayoutMoodForm: LinearLayout
	private lateinit var linearLayoutNameForm: LinearLayout
	private lateinit var seekBarMoodBar: SeekBar
	private lateinit var textViewMoodDescription: TextView

	override fun onCreate(savedInstanceState: Bundle?)
	{
		super.onCreate(savedInstanceState)
		setContentView(R.layout.first_time_setup)

		/* Initialize widgets. */
		editTextNameInputField   = findViewById(R.id.nameInputField)
		imageViewMoodGraphic     = findViewById(R.id.moodGraphic)
		linearLayoutMoodForm     = findViewById(R.id.moodForm)
		linearLayoutNameForm     = findViewById(R.id.nameForm)
		seekBarMoodBar           = findViewById(R.id.moodBar)
		textViewMoodDescription  = findViewById(R.id.moodDescription)

		/* Default mood bar value. */
		seekBarMoodBar.setProgress(50)

		/* UI logic. */

		/* Upon finishing typing in the name, move the form field up. */
		editTextNameInputField.setOnKeyListener(

			View.OnKeyListener { _, key, event ->

				if (key == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP)
				{
					val displayMetrics = DisplayMetrics()
					windowManager.defaultDisplay.getMetrics(displayMetrics)

					val height = displayMetrics.heightPixels.toFloat()

					/* Clear the focus on the form item. */
					editTextNameInputField.clearFocus()

					/* Shift the name form 33% up the screen. */
					ObjectAnimator.ofFloat(linearLayoutNameForm, "translationY", height * -0.33f).apply {
						duration = 500  // milliseconds
						start()
					}

					/* Fade out the name form. */
					ObjectAnimator.ofFloat(linearLayoutNameForm, "alpha", 0.25f).apply {
						duration = 500  // milliseconds
						start()
					}

					/* Make next form visible, but still 0.00 opacity until animated to 1.00. */
					linearLayoutMoodForm.visibility = View.VISIBLE

					/* Wait 300 milliseconds fading in the next prompt. */
					Handler().postDelayed(
						{
							ObjectAnimator.ofFloat(linearLayoutMoodForm, "alpha", 1.00f).apply {
								duration = 300  // milliseconds
								start()
							}
						},
						300  // milliseconds
					)
				}

				false
			}
		)

		/* Upon moving the seek bar, change the image and text dynamically. */
		seekBarMoodBar.setOnSeekBarChangeListener(

			object: SeekBar.OnSeekBarChangeListener
			{ 
				override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean)
				{
					if (progress < 20)
					{
						imageViewMoodGraphic.setImageResource(R.drawable.flower_level_01)
						textViewMoodDescription.text = "Not too great!"
					}
					else if (progress < 40)
					{
						imageViewMoodGraphic.setImageResource(R.drawable.flower_level_02)
						textViewMoodDescription.text = "A little down"
					}
					else if (progress < 60)
					{
						imageViewMoodGraphic.setImageResource(R.drawable.flower_level_03)
						textViewMoodDescription.text = "Alright"
					}
					else if (progress < 80)
					{
						imageViewMoodGraphic.setImageResource(R.drawable.flower_level_04)
						textViewMoodDescription.text = "Good"
					}
					else
					{
						imageViewMoodGraphic.setImageResource(R.drawable.flower_level_05)
						textViewMoodDescription.text = "Great!"
					}
				}

				override fun onStartTrackingTouch(seekBar: SeekBar) {}
				override fun onStopTrackingTouch(seekBar: SeekBar) {}
			}
		)
	}

	companion object
	{
		private const val TAG = "FirstTimeSetup"
	}
}
