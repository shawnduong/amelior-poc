package com.hci_g1.amelior

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.hci_g1.amelior.Fragment.FavoriteFragment
import com.hci_g1.amelior.Fragment.HomeFragment
import com.hci_g1.amelior.Fragment.SettingFragment

class MainMenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_menu_activity)

        //******************action bar*****************
        val actionbar = supportActionBar
        //set actionbar title
        actionbar!!.title = "Amelior"
        //set back button
        actionbar.setDisplayHomeAsUpEnabled(true)

        //***********************fragment*********************
        val homeFragment = HomeFragment()
        val favoriteFragment = FavoriteFragment()
        val settingFragment = SettingFragment()

        makeCurrentFragment(homeFragment)
        findViewById<BottomNavigationView>(R.id.bottom_navigation)
            .setOnNavigationItemSelectedListener {
                when(it.itemId) {
                    R.id.ic_homePage ->makeCurrentFragment(homeFragment)
                    R.id.ic_favoritePage -> makeCurrentFragment(favoriteFragment)
                    R.id.ic_settingPage -> makeCurrentFragment(settingFragment)
                }
                true
            }
    }

    private fun makeCurrentFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fl_wrapper, fragment)
            commit()
        }
    }

    //handle onBack pressed(go to previous page)
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}





