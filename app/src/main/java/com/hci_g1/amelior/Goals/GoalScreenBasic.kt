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
import com.github.mikephil.charting.components.XAxis.*
import com.github.mikephil.charting.components.Legend.*

import com.hci_g1.amelior.entities.Goal
import com.hci_g1.amelior.entities.StepCount
import com.hci_g1.amelior.entities.Distance

class GoalScreenBasic: AppCompatActivity()
{
	private lateinit var goal: Goal

	private lateinit var buttonClose: Button
	private lateinit var imageViewFlowerGraphic: ImageView
	private lateinit var textViewTitle: TextView
	private lateinit var textViewSubtitle: TextView

	/*Database Variables*/
	private lateinit var steps : StepCount
	private lateinit var distance : Distance

	/*Epoch Day*/
	private fun get_epoch_day(): Long
	{
		val millisecPerDay: Long = 1000*60*60*24
		return (System.currentTimeMillis()/millisecPerDay)
	}
	private var today: Long = 0


	override fun onCreate(savedInstanceState: Bundle?)
	{
		super.onCreate(savedInstanceState)
		setContentView(R.layout.goal_screen_basic)

		/* Reading the passed in initCode; this is the goal's DB row (key). */
		val initCode: Int? = getIntent().getIntExtra("initIndex", 0)
		Log.d(TAG, "Loaded with initCode=${initCode}.")

		/*DataBase Initalization*/

		val distanceDao = UserDatabase.getInstance(this).distanceDao
		distance = distanceDao.get_distance_now(19108)

		val stepCountDao = UserDatabase.getInstance(this).stepCountDao
		steps = stepCountDao.get_step_count_now(19108)

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

		/* Graph TESTING TODO */
		/* Check this out: https://weeklycoding.com/mpandroidchart-documentation/getting-started/ */

		/* Find the chart in the XML and get the axes. X depends on Y. */
		val graph: LineChart = findViewById(R.id.lineChart)
		val axisY = graph.getAxisLeft()
		val axisX = graph.getXAxis()

		val axisYRight = graph.getAxisRight()
		axisYRight.setEnabled(false)
		val description = graph.getDescription()
		val timelist: MutableList<Long> = ArrayList()
		val distancelist : MutableList<Distance> = ArrayList()
		val stepcountlist : MutableList<StepCount> = ArrayList()
		val Legendobj = graph.getLegend()
		/* Aesthetic dashed lines on both axes. */
		axisX.enableGridDashedLine(10f, 10f, 0f)
		axisY.enableGridDashedLine(10f, 10f, 0f)
		axisX.setPosition(XAxisPosition.BOTTOM)
		description.setText("")

		/*Legend*/
		Legendobj.setEnabled(true)
		Legendobj.setFormSize(10f)

		//Legendobj.setPosition(Legend.Position.BELOW_CHART_LEFT)
		/*Epoch Generation*/
		today = get_epoch_day()

		/*checks to see if there is a record for the last 5 days*/
		for(i in 4 downTo 0)
		{
			//Log.d(TAG, "test 4 ${distanceDao.distance_exists_now(today-i)}")
			if(distanceDao.distance_exists_now(today-i))
			{
				timelist.add(today-i)
				distancelist.add(distanceDao.get_distance_now(today-i))
			}
			if(stepCountDao.step_count_exists(today-i))
			{
				if(timelist.size < 1)
				{
					timelist.add(today-i)
				}
				stepcountlist.add(stepCountDao.get_step_count_now((today-i)))
			}
		}
		Log.d(TAG, "time ${timelist[0]} distance  ${distancelist[0].totalDistance} steps  ${stepcountlist[0].stepTotal}")

		/* Create the input dataset. */
		val entries: MutableList<Entry> = ArrayList()
		for(i in timelist.indices)
		{
			entries.add(Entry(i+1.toFloat(), distancelist[i].totalDistance))
			//entries.add(Entry(timelist[i].toFloat(), distancelist[i].totalDistance))
			//entries.add(Entry(timelist[i].toFloat(), stepcountlist[i].stepTotal))
		}


		//entries.add(Entry(1.0f, 1.0f))
		/*
		entries.add(Entry(2.0f, 4.0f))
		entries.add(Entry(3.0f, 9.0f))
		entries.add(Entry(4.0f, 16.0f))
		entries.add(Entry(5.0f, 25.0f))
		*/
		/* Create the LineDataSet object used by the chart. */
		val dataset = LineDataSet(entries, "Distance")
		dataset.setColors(Color.BLACK)

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
