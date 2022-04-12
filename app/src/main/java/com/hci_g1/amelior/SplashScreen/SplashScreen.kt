//package com.hci_g1.amelior.SplashScreen
package com.hci_g1.amelior

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
//import com.hci_g1.amelior.MainActivity
//import com.hci_g1.amelior.R

class SplashScreen: AppCompatActivity()
{
	override fun onCreate(savedInstanceState: Bundle?)
	{
		super.onCreate(savedInstanceState)
		setContentView(R.layout.splash_screen)

		/* Wait 3000 milliseconds before moving to the next screen. */
		Handler().postDelayed(
			{
				/* TODO: Only do first time setup the first time. */
				startActivity(Intent(this, MainActivity:: class.java))
				finish()
			},
			3000  // milliseconds
		)
	}
}
