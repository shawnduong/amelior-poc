package com.hci_g1.amelior.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.hci_g1.amelior.R
import kotlinx.android.synthetic.main.fragment_favorite.view.*

class FavoriteFragment : Fragment(R.layout.fragment_favorite){
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val favoriteView = inflater.inflate(R.layout.fragment_favorite, container, false)

        favoriteView.favorite_label.setOnClickListener {
            favoriteView.findNavController().navigate(R.id.from_favoriteFragment_to_settingFragment)
        }

        return favoriteView
    }
}