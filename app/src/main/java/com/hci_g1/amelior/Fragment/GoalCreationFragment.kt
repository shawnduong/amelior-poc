package com.hci_g1.amelior

import android.animation.ObjectAnimator
import android.os.Bundle
import android.os.Handler
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment

import com.hci_g1.amelior.entities.User

class GoalCreationFragment: Fragment()
{
	private val displayMetrics = DisplayMetrics()
	private var screenHeight: Float = 0f

	private lateinit var userDao: UserDao

	private lateinit var textViewGreeting: TextView
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

		/* Initialize widgets. */
		textViewGreeting       = view.findViewById(R.id.greeting)
		textViewPromptGeneral  = view.findViewById(R.id.promptGeneral)

		/* UI logic. */

		/* Get the screen metrics. */
		getActivity()!!.windowManager.defaultDisplay.getMetrics(displayMetrics)
		screenHeight = displayMetrics.heightPixels.toFloat()

		/* Get the username from the database and set the greeting text. */
		val user = userDao.get_user_now("user")
		textViewGreeting.text = "Hello, ${user.name}!"

		/* Fade in the first text view and move it up. */
		ObjectAnimator.ofFloat(textViewGreeting, "alpha", 1.00f).apply {
			duration = 500  // milliseconds
			start()
		}
		ObjectAnimator.ofFloat(textViewGreeting, "translationY", screenHeight * -0.30f).apply {
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
				ObjectAnimator.ofFloat(textViewPromptGeneral, "translationY", (screenHeight * -0.30f) + 10.0f).apply {
					duration = 1000  // milliseconds
					start()
				}
			},
			1500
		)
	}

	companion object
	{
		private val TAG = "GoalCreationFragment"
	}
}
