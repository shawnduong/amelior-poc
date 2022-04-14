package com.hci_g1.amelior

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class FirstTimeOrientation: AppCompatActivity()
{
	private val displayMetrics = DisplayMetrics()

	private var screenHeight: Float = 0f
	private lateinit var buttonPermissionsButton: Button
	private lateinit var textViewOrientationHello01: TextView
	private lateinit var textViewOrientationHello02: TextView
	private lateinit var textViewOrientationHello03: TextView

	override fun onCreate(savedInstanceState: Bundle?)
	{
		super.onCreate(savedInstanceState)
		setContentView(R.layout.first_time_orientation)

		/* Initialize widgets. */
		buttonPermissionsButton    = findViewById(R.id.permissionsButton)
		textViewOrientationHello01 = findViewById(R.id.orientationHello01)
		textViewOrientationHello02 = findViewById(R.id.orientationHello02)
		textViewOrientationHello03 = findViewById(R.id.orientationHello03)

		/* Get the screen metrics. */
		windowManager.defaultDisplay.getMetrics(displayMetrics)
		screenHeight = displayMetrics.heightPixels.toFloat()

		/* Make the first text view visible. */
		textViewOrientationHello01.visibility = View.VISIBLE

		/* Fade in the first text view and move it up. */
		ObjectAnimator.ofFloat(textViewOrientationHello01, "alpha", 1.00f).apply {
			duration = 1000  // milliseconds
			start()
		}
		ObjectAnimator.ofFloat(textViewOrientationHello01, "translationY", -64f).apply {
			duration = 1000  // milliseconds
			start()
		}

		/* Give the user 3000 milliseconds to read "Welcome to Amelior!" */
		Handler().postDelayed(
			{
				/* Bring the first text up and fade it out. */
				ObjectAnimator.ofFloat(textViewOrientationHello01, "translationY", screenHeight * -0.20f).apply {
					duration = 1000  // milliseconds
					start()
				}
				ObjectAnimator.ofFloat(textViewOrientationHello01, "alpha", 0.25f).apply {
					duration = 1000  // milliseconds
					start()
				}

				/* Make the next view visible, fade it in, and move it up. */
				textViewOrientationHello02.visibility = View.VISIBLE
				ObjectAnimator.ofFloat(textViewOrientationHello02, "alpha", 1.00f).apply {
					duration = 1000  // milliseconds
					start()
				}
				ObjectAnimator.ofFloat(textViewOrientationHello02, "translationY", -64f).apply {
					duration = 1000  // milliseconds
					start()
				}
			},
			3000  // milliseconds
		)

		/* Give the user 3000 milliseconds to read "Amelior! helps..." */
		Handler().postDelayed(
			{
				/* Bring the second text up and fade it out. */
				ObjectAnimator.ofFloat(textViewOrientationHello02, "translationY", screenHeight * -0.20f).apply {
					duration = 1000  // milliseconds
					start()
				}
				ObjectAnimator.ofFloat(textViewOrientationHello02, "alpha", 0.25f).apply {
					duration = 1000  // milliseconds
					start()
				}

				/* Make the next view visible, fade it in, and move it up. */
				textViewOrientationHello03.visibility = View.VISIBLE
				ObjectAnimator.ofFloat(textViewOrientationHello03, "alpha", 1.00f).apply {
					duration = 1000  // milliseconds
					start()
				}
				ObjectAnimator.ofFloat(textViewOrientationHello03, "translationY", -64f).apply {
					duration = 1000  // milliseconds
					start()
				}
			},
			6000  // milliseconds
		)

		/* Give the user 3000 milliseconds to view "In order to..." */
		Handler().postDelayed(
			{
				/* Bring the third text up and fade it out. */
				ObjectAnimator.ofFloat(textViewOrientationHello03, "translationY", screenHeight * -0.18f).apply {
					duration = 1000  // milliseconds
					start()
				}
				ObjectAnimator.ofFloat(textViewOrientationHello03, "alpha", 0.25f).apply {
					duration = 1000  // milliseconds
					start()
				}

				/* Make the button visible and fade it in. */
				buttonPermissionsButton.visibility = View.VISIBLE
				ObjectAnimator.ofFloat(buttonPermissionsButton, "alpha", 1.00f).apply {
					duration = 1000  // milliseconds
					start()
				}
			},
			9000  // milliseconds
		)
	}

	companion object
	{
		private const val TAG = "FirstTimeOrientation"
	}
}
