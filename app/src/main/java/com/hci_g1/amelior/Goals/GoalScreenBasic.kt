package com.hci_g1.amelior

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.*
import com.github.mikephil.charting.charts.*
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.*
import com.github.mikephil.charting.components.*
import com.github.mikephil.charting.components.Legend.*
import com.github.mikephil.charting.components.XAxis.*

import com.hci_g1.amelior.entities.Goal
import com.hci_g1.amelior.entities.Distance
import com.hci_g1.amelior.entities.StepCount

class GoalScreenBasic: AppCompatActivity()
{
	private var today: Long = 0

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

		today = get_epoch_day()

		/* Initialize widgets. */
		buttonClose             = findViewById(R.id.close)
		imageViewFlowerGraphic  = findViewById(R.id.flowerGraphic)
		textViewTitle           = findViewById(R.id.title)
		textViewSubtitle        = findViewById(R.id.subtitle)

		/* Fill in the headers. */
		if ((goal.custom) && (goal.quantity == -1))
		{
			/* Non-numerical custom goal format is different due to lack of quantity and units.  */
			textViewTitle.text = "${goal.action.capitalize()}"
			textViewSubtitle.text = "every ${goal.frequency}"
		}
		else
		{
			textViewTitle.text = "${goal.action.capitalize()} ${goal.quantity} ${goal.units}"
			textViewSubtitle.text = "every ${goal.frequency}"
		}

		/* Set the image based on the level. */
		if      (goal.level == 1)  imageViewFlowerGraphic.setImageResource(R.drawable.flower_level_01)
		else if (goal.level == 2)  imageViewFlowerGraphic.setImageResource(R.drawable.flower_level_02)
		else if (goal.level == 3)  imageViewFlowerGraphic.setImageResource(R.drawable.flower_level_03)
		else if (goal.level == 4)  imageViewFlowerGraphic.setImageResource(R.drawable.flower_level_04)
		else if (goal.level == 5)  imageViewFlowerGraphic.setImageResource(R.drawable.flower_level_05)
		else                       imageViewFlowerGraphic.setImageResource(R.drawable.flower_level_05)

		/* UI logic. */

		/* Upon clicking the "GO BACK" button, go back to the main page. */
		buttonClose.setOnClickListener {
			Log.d(TAG, "Moving to Dashboard.")
			startActivity(Intent(this, Dashboard::class.java))
			finish()
		}

		/* Find the chart in the XML and get the axes. */
		val graph: LineChart = findViewById(R.id.lineChart)
		val axisY = graph.getAxisLeft()
		val axisX = graph.getXAxis()

		val days = arrayOf(
			"S", "M", "T", "W", "R", "F", "S",
			"S", "M", "T", "W", "R", "F", "S",
		)

		/* Set the X axis labels to the days of the week. */
		axisX.setValueFormatter(

			object: ValueFormatter()
			{
				override fun getAxisLabel(value: Float, axis: AxisBase): String
				{
					/* I'm not proud of this but we need to get the MVP. Epoch
					 * day 19106 was a Sunday and is used as a reference. This
					 * technique should never be used, ever.
					 */
					Log.d(TAG, "${get_epoch_day().toString()}")

					return days[((get_epoch_day().toInt()-19106) % 7) + (value.toInt()+7)]
				}
			}
		)

		/* Disable the right Y axis. */
		graph.getAxisRight().setEnabled(false)

		/* The Y axis must start at 0. */
		axisY.setAxisMinValue(0.0f)

		/* Because the time scale is only weekly, X e [-7, 1]. There is a day
		   of extra padding in both directions for aesthetics. */
		axisX.setAxisMinValue(-6.0f)
		axisX.setAxisMaxValue(1.0f)

		/* Set the description text. */
		graph.getDescription().setText("")

		/* Aesthetic dashed lines on both axes. */
		axisX.enableGridDashedLine(10.0f, 10.0f, 0.0f)
		axisY.enableGridDashedLine(10.0f, 10.0f, 0.0f)

		/* Position the X axis on the bottom. */
		axisX.setPosition(XAxisPosition.BOTTOM)

		/* Disable the legend */
		graph.getLegend().setEnabled(false)

		/* Set the Y axis labels to "Done" or "Not Done," but only if it's non-numeric. */
		if (goal.quantity == -1)
		{
			axisY.setAxisMaxValue(1.25f)

			axisY.setValueFormatter(

				object: ValueFormatter()
				{
					override fun getAxisLabel(value: Float, axis: AxisBase): String
					{
						if      (value == 0.0f)  return "Not Done"
						else if (value == 1.0f)  return "Done"
						else                     return ""
					}
				}
			)
		}

		/* Entries populated by iterating through the past week of data. */
		val entries: MutableList<Entry> = ArrayList()
		var dataType: String = ""

		/* Iterate different DAOs based on the type of goal. */
		if (goal.units == "m")
		{
			val distanceDao: DistanceDao = UserDatabase.getInstance(this).distanceDao
			dataType = "Distance"

			for (day in (today-6)..(today))
			{
				if (distanceDao.distance_exists_now(day))
				{
					entries.add(Entry((today-day).toFloat() * -1.0f, distanceDao.get_distance_now(day).totalDistance.toFloat()))
				}
			}
		}
		else if (goal.units == "steps")
		{
			val stepCountDao: StepCountDao = UserDatabase.getInstance(this).stepCountDao
			dataType = "Steps"

			for (day in (today-6)..(today))
			{
				if (stepCountDao.step_count_exists_now(day))
				{
					entries.add(Entry((today-day).toFloat() * -1.0f, stepCountDao.get_step_count_now(day).totalSteps.toFloat()))
				}
			}
		}
		/* Actual spaghetti. */
		else
		{
			/* Fill in the last 5 days of data. */
			entries.add(Entry(-6.0f, goal.hist6.toFloat()))
			entries.add(Entry(-5.0f, goal.hist5.toFloat()))
			entries.add(Entry(-4.0f, goal.hist4.toFloat()))
			entries.add(Entry(-3.0f, goal.hist3.toFloat()))
			entries.add(Entry(-2.0f, goal.hist2.toFloat()))
			entries.add(Entry(-1.0f, goal.hist1.toFloat()))
			entries.add(Entry( 0.0f, goal.hist0.toFloat()))
		}

		/* Create the LineDataSet object used by the chart. */
		val dataset = LineDataSet(entries, dataType)

		/* If it's non-numeric, don't display values. */
		if (goal.quantity == -1)  dataset.setDrawValues(false)

		/* Make the line black with size 2.0f circles. */
		dataset.setColor(Color.BLACK)
		dataset.setCircleColor(Color.BLACK)
		dataset.setCircleRadius(2.0f)
		dataset.setDrawCircleHole(false)

		/* Make the graph filled. */
		dataset.setFillDrawable(getResources().getDrawable(R.drawable.teal_blue_lighter_bg))
		dataset.setDrawFilled(true)

		/* Plot the data on the graph. */
		graph.setData(LineData(dataset))
	}

	/* Get the current epoch day. */
	private fun get_epoch_day(): Long
	{
		return System.currentTimeMillis()/(1000*60*60*24)
	}

	/* Get the current epoch day given an epoch time (ms). */
	private fun get_epoch_day(time: Long): Long
	{
		return time/(1000*60*60*24)
	}

	companion object
	{
		private val TAG = "GoalScreenBasic"
	}
}
