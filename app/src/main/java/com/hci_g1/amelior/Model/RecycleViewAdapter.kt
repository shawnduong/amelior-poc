package com.hci_g1.amelior.Model

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

import com.hci_g1.amelior.R
import kotlinx.android.synthetic.main.recycleview_model.view.*

class RecycleViewAdapter: RecyclerView.Adapter<RecycleViewAdapter.ViewHolder>() {

    private val itemTitles = arrayOf("Step Count", "Distance Count", "Goal")

    private val itemDetails = arrayOf("Step Count is a counting step you walk", "Distance Count is a counting distance you walk", "Goal you can set")

    private val itemImages = intArrayOf(R.drawable.img_1, R.drawable.img, R.drawable.img_1)

    inner class ViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) //, View.OnClickListener
    {
        var image: ImageView = itemView.item_image
        var textTitle: TextView = itemView.item_title
        var textDes: TextView = itemView.item_details

//        init {
            //these three line below can be replace from only have var textDes: TextView to var textDes: TextView = itemView.item_details
//            image = itemView.findViewById(R.id.item_image)
//            textTitle = itemView.findViewById(R.id.item_title)
//            textDes = itemView.findViewById(R.id.item_details)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycleview_model, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textTitle.text = itemTitles[position]
        holder.textDes.text = itemDetails[position]
        holder.image.setImageResource(itemImages[position])
        holder.itemView.setOnClickListener {
                v: View -> Toast.makeText(v.context, "Clicked on $position the item", Toast.LENGTH_SHORT).show()

        }
    }

    override fun getItemCount(): Int {
        return itemTitles.size
    }

}