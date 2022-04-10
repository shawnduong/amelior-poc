package com.hci_g1.amelior

import android.app.Service
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

/* GPS service writes GPS updates to database while service is bound. */
class Gps: Service()
{
	/* Required service objects init later at creation to reduce start time. */
	private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
	private lateinit var locationCallback: LocationCallback
	private lateinit var locationRequest: LocationRequest

	/* Service binders. */
	private val binder = _Binder()
	inner class _Binder: Binder()
	{
		fun get_service(): Gps = this@Gps
	}

	/* Get binder on bind. */
	override fun onBind(intent: Intent): IBinder
	{
		Log.d(TAG, "Binding to GPS service.")
		return binder
	}

	/* Initialize required service objects at creation time to reduce start time. */
	override fun onCreate()
	{
		Log.d(TAG, "Initializing required service objects...")

		/* Primary location provider. */
		fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

		/* Callback function definitions. */
		locationCallback = object: LocationCallback()
		{
			/* Save the location to a DB upon getting a location result. */
			override fun onLocationResult(locationResult: LocationResult)
			{
				super.onLocationResult(locationResult)

				/* TODO: Integreate this with Crystal's DB code. */
				var location = locationResult.lastLocation
				Log.d(TAG, "Location acquired: " + location.toString())
			}
		}

		/* Location request wants high accuracy results every 5 seconds. */
		locationRequest = LocationRequest.create().apply {
			priority = LocationRequest.PRIORITY_HIGH_ACCURACY
			interval = 5000  // milliseconds
		}

		Log.d(TAG, "Initialization complete.")
	}

	/* Subscribe to GPS updates. */
	fun subscribe(): Boolean
	{
		Log.d(TAG, "Subscribing to GPS updates...")

		try
		{
			fusedLocationProviderClient.requestLocationUpdates(
				locationRequest, locationCallback, Looper.getMainLooper()
			)

			Log.d(TAG, "Subscribed to GPS updates.")
			return true
		}
		catch (e: Exception)
		{
			Log.e(TAG, "Could not successfully subscribe to GPS updates.")
		}

		return false
	}

	/* Unsubscribe from GPS updates. */
	fun unsubscribe(): Boolean
	{
		Log.d(TAG, "Unsubscribing from GPS updates...")

		try
		{
			fusedLocationProviderClient.removeLocationUpdates(locationCallback)
			Log.d(TAG, "Unsubscribed from GPS updates.")
			return true
		}
		catch (e: Exception)
		{
			Log.e(TAG, "Could not successfully unsubscribe from GPS updates.")
		}

		return false
	}

	companion object
	{
		/* Logging tag. */
		private const val TAG = "GPS"
	}
}
