package com.hci_g1.amelior

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.*
import com.github.mikephil.charting.charts.*
import com.github.mikephil.charting.data.*

import com.hci_g1.amelior.entities.Goal

class GoalScreenBasic: AppCompatActivity()
{
	private lateinit var goal: Goal

	private lateinit var buttonClose: Button
	private lateinit var imageViewFlowerGraphic: ImageView
	private lateinit var textViewTitle: TextView
	private lateinit var textViewSubtitle: TextView

	override fun onCreate(savedInstanceState: Bundle?)
	{
		super.onCreate(savedInstanceState)
		setContentView(R.layout.goal_screen_basic)

		/* Reading the passed in initCode; this is the goal's DB row (key). */
		val initCode: Int? = getIntent().getIntExtra("initIndex", 0)
		Log.d(TAG, "Loaded with initCode=${initCode}.")

		/* Initialize variables. */
		if (initCode != null)
		{
			val goalDao = UserDatabase.getInstance(this).goalDao
			goal = goalDao.get_goal_now(initCode)
			Log.d(TAG, "Goal successfully loaded.")
		}

		/* Initialize widgets. */
		buttonClose             = findViewById(R.id.close)
		imageViewFlowerGraphic  = findViewById(R.id.flowerGraphic)
		textViewTitle           = findViewById(R.id.title)
		textViewSubtitle        = findViewById(R.id.subtitle)

		/* Fill in the data. */
		textViewTitle.text = "${goal.action.capitalize()} ${goal.quantity} ${goal.units}"
		textViewSubtitle.text = "every ${goal.frequency}"

		/* Set the image based on the level. */
		if      (goal.level == 1)  imageViewFlowerGraphic.setImageResource(R.drawable.flower_level_01)
		else if (goal.level == 2)  imageViewFlowerGraphic.setImageResource(R.drawable.flower_level_02)
		else if (goal.level == 3)  imageViewFlowerGraphic.setImageResource(R.drawable.flower_level_03)
		else if (goal.level == 4)  imageViewFlowerGraphic.setImageResource(R.drawable.flower_level_04)
		else if (goal.level == 5)  imageViewFlowerGraphic.setImageResource(R.drawable.flower_level_05)
		/* 3 is the default level. */
		else                       imageViewFlowerGraphic.setImageResource(R.drawable.flower_level_03)

		/* UI logic. */

		/* Upon clicking the "GO BACK" button, go back to the main page. */
		buttonClose.setOnClickListener {
			Log.d(TAG, "Moving to Dashboard.")
			startActivity(Intent(this, Dashboard::class.java))
			finish()
		}

		/* Graph TESTING TODO */

		/* Check this out: https://weeklycoding.com/mpandroidchart-documentation/getting-started/ */

		/* Find the chart in the XML and get the axes. X depends on Y. */
		val graph: LineChart = findViewById(R.id.lineChart)
		val axisY = graph.getAxisLeft()
		val axisX = graph.getAxis(axisY.axisDependency)

		/* Aesthetic dashed lines on both axes. */
		axisX.enableGridDashedLine(10f, 10f, 0f)
		axisY.enableGridDashedLine(10f, 10f, 0f)

		/* Create the input dataset. */
		val entries: MutableList<Entry> = ArrayList()
		entries.add(Entry(0.0f, 0.0f))
		entries.add(Entry(1.0f, 1.0f))
		entries.add(Entry(2.0f, 4.0f))
		entries.add(Entry(3.0f, 9.0f))
		entries.add(Entry(4.0f, 16.0f))
		entries.add(Entry(5.0f, 25.0f))

		/* Create the LineDataSet object used by the chart. */
		val dataset = LineDataSet(entries, "sample data")

		/* A LineDataSet is not the same as LineData. The chart needs LineData.
		   We pass LineDataSet through LineData() and pass it into the setData
		   method to actually plot the data on the graph. */
		graph.setData(LineData(dataset))

		/* I hope this helped. -Shawn */
	}

	companion object
	{
		private val TAG = "GoalScreenBasic"
	}
}
