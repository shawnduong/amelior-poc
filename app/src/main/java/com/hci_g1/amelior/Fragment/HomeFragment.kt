package com.hci_g1.amelior

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.*
import kotlinx.coroutines.NonCancellable.start

import com.hci_g1.amelior.entities.Goal
import com.hci_g1.amelior.entities.Mood
import com.hci_g1.amelior.entities.User

class HomeFragment: Fragment()
{
	private val displayMetrics = DisplayMetrics()
	private var screenHeight: Float = 0f

	private lateinit var goals: MutableList<Goal>

	private lateinit var goalDao: GoalDao
	private lateinit var moodDao: MoodDao
	private lateinit var distanceDao: DistanceDao
	private lateinit var stepCountDao: StepCountDao

	private lateinit var buttonSplashSubmit: Button
	private lateinit var imageViewMoodGraphic: ImageView
	private lateinit var linearLayoutMoodForm: LinearLayout
	private lateinit var recyclerViewGoalRecycler: RecyclerView
	private lateinit var seekBarMoodBar: SeekBar
	private lateinit var textViewMoodDescription: TextView

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
	{
		return inflater.inflate(R.layout.fragment_home, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?)
	{
		super.onViewCreated(view, savedInstanceState)
		val context = getContext()

		/* Initializing variables. */

		if (context != null)
		{
			goalDao = UserDatabase.getInstance(context).goalDao
			Log.d(TAG, "Goal database successfully loaded.")

			moodDao = UserDatabase.getInstance(context).moodDao
			Log.d(TAG, "Mood database successfully loaded.")

			distanceDao = UserDatabase.getInstance(context).distanceDao
			Log.d(TAG, "Distance database successfully loaded.")

			stepCountDao = UserDatabase.getInstance(context).stepCountDao
			Log.d(TAG, "Step count database successfully loaded.")
		}

		goals = ArrayList()
		for (i in 0..goalDao.size()-1)
		{
			goals.add(goalDao.get_goal_now(i))
		}

		/* Initialize widgets. */
		buttonSplashSubmit        = view.findViewById(R.id.splashSubmit)
		imageViewMoodGraphic      = view.findViewById(R.id.moodGraphic)
		linearLayoutMoodForm      = view.findViewById(R.id.moodForm)
		recyclerViewGoalRecycler  = view.findViewById(R.id.goalRecycler)
		seekBarMoodBar            = view.findViewById(R.id.moodBar)
		textViewMoodDescription   = view.findViewById(R.id.moodDescription)

		/* Create the goals recycle view. */
		recyclerViewGoalRecycler.apply {
			layoutManager = LinearLayoutManager(getContext())
			adapter = GoalAdapter(goalDao, distanceDao, stepCountDao, goals)
		}

		/* Default mood bar value. */
		seekBarMoodBar.setProgress(50)

		/* Get the screen metrics. */
		requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
		screenHeight = displayMetrics.heightPixels.toFloat()

		seekBarMoodBar.setOnSeekBarChangeListener(

			object: SeekBar.OnSeekBarChangeListener
			{
				override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean)
				{
					/* Make the submit button visible and fade it in. */
					if (buttonSplashSubmit.visibility == View.INVISIBLE)
					{
						buttonSplashSubmit.visibility = View.VISIBLE

						ObjectAnimator.ofFloat(buttonSplashSubmit, "alpha", 1.00f).apply {
							duration = 100	// milliseconds
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

		/* Upon clicking the submit button, save input to database. */
		buttonSplashSubmit.setOnClickListener {
			val mood: Int = seekBarMoodBar.getProgress()
			moodDao.insert_mood_now(Mood(moodDao.size(), System.currentTimeMillis(), mood))
		}

		/* Upon clicking the mood graphic, go to the mood page. */
		imageViewMoodGraphic.setOnClickListener {
			view.context.startActivity(Intent(view.context, GoalScreenMood::class.java))
		}
	}

	companion object
	{
		private val TAG = "HomeFragment"
	}
}
