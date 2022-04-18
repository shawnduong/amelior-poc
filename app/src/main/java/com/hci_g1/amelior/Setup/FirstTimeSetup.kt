package com.hci_g1.amelior

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.DisplayMetrics
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

import com.hci_g1.amelior.entities.Mood
import com.hci_g1.amelior.entities.User

class FirstTimeSetup: AppCompatActivity()
{
	private lateinit var moodDao: MoodDao
	private lateinit var userDao: UserDao

	private lateinit var buttonSplashProceed: Button
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

		/* Initialize variables. */
		moodDao = UserDatabase.getInstance(this).moodDao
		userDao = UserDatabase.getInstance(this).userDao

		/* Initialize widgets. */
		buttonSplashProceed      = findViewById(R.id.splashProceed)
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

					/* Wait 300 milliseconds fading in the next prompt. This gives it time for the previous
					   prompt to move up and fade out first. */
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
					/* Make the proceed button visible and fade it in. */
					if (buttonSplashProceed.visibility == View.INVISIBLE)
					{
						buttonSplashProceed.visibility = View.VISIBLE

						ObjectAnimator.ofFloat(buttonSplashProceed, "alpha", 1.00f).apply {
							duration = 300  // milliseconds
							start()
						}
					}

					if (progress < 20)
					{
						imageViewMoodGraphic.setImageResource(R.drawable.emoji_level_01)
						textViewMoodDescription.text = "Not too great!"
					}
					else if (progress < 40)
					{
						imageViewMoodGraphic.setImageResource(R.drawable.emoji_level_02)
						textViewMoodDescription.text = "A little down"
					}
					else if (progress < 60)
					{
						imageViewMoodGraphic.setImageResource(R.drawable.emoji_level_03)
						textViewMoodDescription.text = "Alright"
					}
					else if (progress < 80)
					{
						imageViewMoodGraphic.setImageResource(R.drawable.emoji_level_04)
						textViewMoodDescription.text = "Good"
					}
					else
					{
						imageViewMoodGraphic.setImageResource(R.drawable.emoji_level_05)
						textViewMoodDescription.text = "Great!"
					}
				}

				override fun onStartTrackingTouch(seekBar: SeekBar) {}
				override fun onStopTrackingTouch(seekBar: SeekBar) {}
			}
		)

		/* Upon clicking the proceed button, save input to database and go to orientation. */
		buttonSplashProceed.setOnClickListener {

			val name: String = editTextNameInputField.text.toString()
			val mood: Int = seekBarMoodBar.getProgress()

			moodDao.insert_mood_now(Mood(0, System.currentTimeMillis(), mood))
			userDao.insert_user_now(User("user", name))

			startActivity(Intent(this, FirstTimeOrientation::class.java))
			finish()
		}
	}

	companion object
	{
		private const val TAG = "FirstTimeSetup"
	}
}
