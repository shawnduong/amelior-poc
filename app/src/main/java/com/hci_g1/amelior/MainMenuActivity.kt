package com.hci_g1.amelior

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.hci_g1.amelior.Fragment.FavoriteFragment
import com.hci_g1.amelior.Fragment.HomeFragment
import com.hci_g1.amelior.Fragment.SettingFragment

class MainMenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_menu_activity)

        /*
            Get the NavController from the NavHost this activity contains
            so we can plug in the navigation bar to it.
        */
        val navHost = supportFragmentManager.findFragmentById(R.id.NavigationHostFragment)
        val navController = navHost?.findNavController()
        val bottomNavBar = findViewById<BottomNavigationView>(R.id.bottom_navigation_bar)
        if (navController != null)
        {
            NavigationUI.setupWithNavController(bottomNavBar, navController)
        }
    }
}





