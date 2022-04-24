package com.hci_g1.amelior

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
		val textViewGoalTitle: TextView

		/* Initialize widgets. */
		init
		{
			textViewGoalTitle = view.findViewById(R.id.goalTitle)
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
		holder.textViewGoalTitle.text = "GOAL: ${d.action} ${d.quantity} ${d.units} every ${d.frequency}"
	}

	override fun getItemCount() = data.size
}
