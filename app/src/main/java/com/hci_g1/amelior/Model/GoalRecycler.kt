package com.hci_g1.amelior

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView

import com.hci_g1.amelior.entities.Goal

class GoalAdapter(private val data: MutableList<Goal>): RecyclerView.Adapter<GoalAdapter.ViewHolder>()
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

		/* Set the image based on the level. */
		if (d.level == 1)       holder.imageViewFlowerGraphic.setImageResource(R.drawable.flower_level_01_small)
		else if (d.level == 2)  holder.imageViewFlowerGraphic.setImageResource(R.drawable.flower_level_02_small)
		else if (d.level == 3)  holder.imageViewFlowerGraphic.setImageResource(R.drawable.flower_level_03_small)
		else if (d.level == 4)  holder.imageViewFlowerGraphic.setImageResource(R.drawable.flower_level_04_small)
		else if (d.level == 5)  holder.imageViewFlowerGraphic.setImageResource(R.drawable.flower_level_05_small)
		else                    holder.imageViewFlowerGraphic.setImageResource(R.drawable.flower_level_03_small)

		/* If the goal is custom, give it either a checkbox or a "+" UI element. */
		if (d.custom)
		{
			holder.imageViewCheck.visibility = View.VISIBLE

			/* If the goal is non-numerical, replace the plus icon with a checkmark. */
			if (d.quantity == -1)
			{
				/* TODO: replace the icon. */
				holder.imageViewCheck.setImageResource(R.drawable.navbar_settings_icon)
			}
		}

		/* Upon clicking the goal, go to the screen for it. */
		holder.relativeLayoutGoalCard.setOnClickListener { view ->
			Log.d(TAG, "Moving to goal screen.")
			val intent = Intent(view.context, GoalScreenBasic::class.java)
			intent.putExtra("initIndex", index)
			view.context.startActivity(intent)
		}
	}

	override fun getItemCount() = data.size

	companion object
	{
		private val TAG = "GoalRecycler"
	}
}
