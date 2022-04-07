package com.hci_g1.amelior

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.os.Looper
import android.util.Log
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices

class GPS: Service()
{
	private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
	private lateinit var locationCallback: LocationCallback
	private lateinit var locationRequest: LocationRequest

	private val binder = _Binder()
	inner class _Binder: Binder()
	{
		fun get_service(): GPS = this@GPS
	}

	override fun onBind(intent: Intent): IBinder
	{
		return binder
	}

	override fun onCreate()
	{
		fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

		locationCallback = object: LocationCallback()
		{
			/* Save the location to a DB. */
			override fun onLocationResult(locationResult: LocationResult)
			{
				super.onLocationResult(locationResult)

				/* TODO: Integreate this with Crystal's DB code. */
				var location = locationResult.lastLocation
				Log.d(TAG, "Location acquired! It is: " + location.toString())
			}
		}

		locationRequest = LocationRequest.create().apply {
			priority = LocationRequest.PRIORITY_HIGH_ACCURACY
			interval = 5000  // milliseconds
		}
	}

	/* Subscribe to the GPS. */
	fun subscribe(): Boolean
	{
		try
		{
			fusedLocationProviderClient.requestLocationUpdates(
				locationRequest, locationCallback, Looper.getMainLooper()
			)

			Log.d(TAG, "Subscribed to GPS updates.")
			return true
		}
		catch (unlikely: SecurityException)
		{
			Log.e(TAG, "Error in subscribe()")
		}

		return false
	}

	/* Unsubscribe to the GPS. */
	fun unsubscribe(): Boolean
	{
		try
		{
			fusedLocationProviderClient.removeLocationUpdates(locationCallback)
			Log.d(TAG, "Unsubscribed from GPS updates.")
			return true
		}
		catch (unlikely: SecurityException)
		{
			Log.e(TAG, "Error in subscribe()")
		}

		return false
	}

	companion object
	{
		private const val TAG = "GPS"
	}
}
