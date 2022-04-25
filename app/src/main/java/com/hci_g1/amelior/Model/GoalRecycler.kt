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

		holder.textViewGoalTitle.setOnClickListener { view ->
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
