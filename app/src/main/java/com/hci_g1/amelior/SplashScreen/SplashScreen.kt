package com.hci_g1.amelior.SplashScreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.hci_g1.amelior.MainActivity
import com.hci_g1.amelior.R

class SplashScreen : AppCompatActivity() {
    lateinit var handler: Handler
    private val SPLASH_TIME: Long = 3000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen_activity)

        handler = Handler()
        handler.postDelayed({
            val intent = Intent(this, MainActivity:: class.java)
            startActivity(intent)
            finish()
        }, SPLASH_TIME) //delaying 3s to open mainactivity

    }
}