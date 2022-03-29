package com.hci_g1.amelior

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.add
import androidx.fragment.app.commit

class MainActivity: AppCompatActivity()
{
	/** LIFECYCLE CALLBACKS **/
	override fun onCreate(savedInstanceState: Bundle?)
	{
		// Recover UI state if this activity was killed
		// See https://developer.android.com/guide/components/activities/activity-lifecycle#instance-state
		super.onCreate(savedInstanceState)

		setContentView(R.layout.main_activity)

		// Host the StepDisplay fragment whenever we instantiate this activity
		// NOTE: If we don't
		if(savedInstanceState == null)
		{
			supportFragmentManager.commit {
				setReorderingAllowed(true)
				add<StepDisplay>(R.id.step_display_container_view)
			}
		}
	}
}
