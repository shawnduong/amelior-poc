package com.hci_g1.amelior

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.widget.Toast

class GPS: Service()
{
	override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int
	{
		Toast.makeText(
			applicationContext,
			"GPS onStartCommand() called.",
			Toast.LENGTH_SHORT
		).show()

		return START_STICKY;
	}

	override fun onBind(intent: Intent): IBinder?
	{
		throw UnsupportedOperationException("Not yet implemented")
	}
}
