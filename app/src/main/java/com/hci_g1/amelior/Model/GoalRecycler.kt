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
		var imageViewFlowerGraphic: ImageView
		val relativeLayoutGoalCard: RelativeLayout
		val textViewGoalTitle: TextView

		/* Initialize widgets. */
		init
		{
			imageViewFlowerGraphic  = view.findViewById(R.id.flowerGraphic)
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
		holder.textViewGoalTitle.text = "${d.action.capitalize()} ${d.quantity} ${d.units}/${d.frequency}"

		/* Set the image based on the level. */
		if (d.level == 1)
		{
			holder.imageViewFlowerGraphic.setImageResource(R.drawable.flower_level_01_small)
		}
		else if (d.level == 2)
		{
			holder.imageViewFlowerGraphic.setImageResource(R.drawable.flower_level_02_small)
		}
		else if (d.level == 3)
		{
			holder.imageViewFlowerGraphic.setImageResource(R.drawable.flower_level_03_small)
		}
		else if (d.level == 4)
		{
			holder.imageViewFlowerGraphic.setImageResource(R.drawable.flower_level_04_small)
		}
		else if (d.level == 5)
		{
			holder.imageViewFlowerGraphic.setImageResource(R.drawable.flower_level_05_small)
		}
		/* 3 is the default level. */
		else
		{
			holder.imageViewFlowerGraphic.setImageResource(R.drawable.flower_level_03_small)
		}

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
