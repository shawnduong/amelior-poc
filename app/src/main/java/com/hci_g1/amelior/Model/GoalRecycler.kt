package com.hci_g1.amelior

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView

import com.hci_g1.amelior.entities.Goal

class GoalAdapter(private val dao: GoalDao, private val data: MutableList<Goal>): RecyclerView.Adapter<GoalAdapter.ViewHolder>()
{
	/* Holder for a single list item. */
	class ViewHolder(view: View): RecyclerView.ViewHolder(view)
	{
		val imageViewFlowerGraphic: ImageView
		val imageViewCheck: ImageView
		val relativeLayoutGoalCard: RelativeLayout
		val textViewGoalTitle: TextView

		/* Initialize widgets. */
		init
		{
			imageViewFlowerGraphic  = view.findViewById(R.id.flowerGraphic)
			imageViewCheck          = view.findViewById(R.id.check)
			relativeLayoutGoalCard  = view.findViewById(R.id.goalCard)
			textViewGoalTitle       = view.findViewById(R.id.goalTitle)
		}
	}

	/* Create an individual item. */
	override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder
	{
		return ViewHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.goal_card, viewGroup, false))
	}

	/* Define the contents of each individual card. */
	override fun onBindViewHolder(holder: ViewHolder, index: Int)
	{
		val d: Goal = data[index]

		/* TODO: calculate epoch time day delta since last completion, then
		   lose levels based on that. */

		/* Set the text. */
		if ((d.custom) && (d.quantity == -1))
		{
			/* Non-numerical custom goal format is different due to lack of quantity and units.  */
			holder.textViewGoalTitle.text = "${d.action.capitalize()} every ${d.frequency}"
		}
		else
		{
			holder.textViewGoalTitle.text = "${d.action.capitalize()} ${d.quantity} ${d.units}/${d.frequency}"
		}

		update_image(holder, d)

		/* If the goal is custom, give it either a checkbox or a "+" UI element. */
		if (d.custom)
		{
			holder.imageViewCheck.visibility = View.VISIBLE

			/* If the goal is numerical, replace the checkmark with a plus. */
			if (d.quantity != -1)
			{
				holder.imageViewCheck.setImageResource(R.drawable.plus)
			}
			/* If the goal is non-numerical, and it was done in the past day, it's already checked. */
			else if (get_epoch_day(d.lastCompleted) == get_epoch_day())
			{
				holder.imageViewCheck.setImageResource(R.drawable.tick_checked)
			}
		}

		/* Upon clicking the goal, go to the screen for it. */
		holder.relativeLayoutGoalCard.setOnClickListener { view ->
			Log.d(TAG, "Moving to goal screen.")
			val intent = Intent(view.context, GoalScreenBasic::class.java)
			intent.putExtra("initIndex", index)
			view.context.startActivity(intent)
		}

		/* Upon clicking the check mark, handle goal completion or progress. */
		holder.imageViewCheck.setOnClickListener {

			/* If the goal is non-numerical, and it was done in the past day, it's already checked. */
			if (get_epoch_day(d.lastCompleted) == get_epoch_day())
			{
				Log.d(TAG, "Already completed goal: ${d.action} every ${d.frequency}")
			}
			/* If the goal is non-numerical, it's done. */
			else if (d.quantity == -1)
			{
				Log.d(TAG, "Completed goal: ${d.action} every ${d.frequency}")
				d.lastCompleted = System.currentTimeMillis()

				/* Level up, if possible. */
				if (d.level < 5)
				{
					Log.d(TAG, "Level up! ${d.action} every ${d.frequency} (lvl ${d.level}-> lvl ${d.level+1})")
					d.level += 1
					update_image(holder, d)
				}

				holder.imageViewCheck.setImageResource(R.drawable.tick_checked)

				/* Save the new data to the database. */
				dao.insert_goal_now(d)
			}
		}
	}

	override fun getItemCount() = data.size

	/* Update all the images in the listing. */
	private fun update_image(holder: ViewHolder, d: Goal)
	{
		if      (d.level == 1)  holder.imageViewFlowerGraphic.setImageResource(R.drawable.flower_level_01_small)
		else if (d.level == 2)  holder.imageViewFlowerGraphic.setImageResource(R.drawable.flower_level_02_small)
		else if (d.level == 3)  holder.imageViewFlowerGraphic.setImageResource(R.drawable.flower_level_03_small)
		else if (d.level == 4)  holder.imageViewFlowerGraphic.setImageResource(R.drawable.flower_level_04_small)
		else if (d.level == 5)  holder.imageViewFlowerGraphic.setImageResource(R.drawable.flower_level_05_small)
		else                    holder.imageViewFlowerGraphic.setImageResource(R.drawable.flower_level_03_small)
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
		private val TAG = "GoalRecycler"
	}
}
