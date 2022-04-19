package com.hci_g1.amelior.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hci_g1.amelior.Model.RecycleViewAdapter
import kotlinx.android.synthetic.main.fragment_home.*
import com.hci_g1.amelior.R

class HomeFragment: Fragment() {

    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycleView.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = RecycleViewAdapter()
        }

        // TODO: Implement onClickListeners inside recycleview that use the navHost provided in MainMenuActivity.kt
    }
}
