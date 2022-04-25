package com.hci_g1.amelior

import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import android.content.Intent
import android.location.Location
import android.os.Looper
import android.util.Log
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.hci_g1.amelior.entities.Distance

/* GPS service writes GPS updates to database while service is bound. */
class Gps: LifecycleService()
{
	/* Required service objects init later at creation to reduce start time. */
	private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
	private lateinit var locationCallback: LocationCallback
	private lateinit var locationRequest: LocationRequest
	
	/* Vars to track a rolling distance total from GPS samples. */
	private lateinit var lastSavedLocation: Location
	private var acquiredStartLocation: Boolean = false
	private var totalDistance: Float = 0f
	
	/* Objects and values for database interaction. */
	private lateinit var distanceDao: DistanceDao
	private lateinit var todaysDistance: Distance
	private var today: Long = 0
	
	private fun get_epoch_day(): Long
	{
		val millisecPerDay: Long = 1000*60*60*24
		return (System.currentTimeMillis()/millisecPerDay)
	}

	/* Initialize required service objects at creation time to reduce start time. */
	override fun onCreate()
	{
		super.onCreate()
		Log.d(TAG, "Initializing required service objects...")

		/* Primary location provider. */
		fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
		
		/* Init database objects and variables */
		initTodaysDistance()

		/* Callback function definitions. */
		locationCallback = object: LocationCallback()
		{
			/* Save the location to a DB upon getting a location result. */
			override fun onLocationResult(locationResult: LocationResult)
			{
				super.onLocationResult(locationResult)
				
				/* TODO: Integreate this with Crystal's DB code.
				*	  */
				var location = locationResult.lastLocation
				Log.d(TAG, "Location acquired: " + location.toString())
				
				if(acquiredStartLocation)
				{
					totalDistance += lastSavedLocation.distanceTo(location)
					lastSavedLocation = location
					Log.d(TAG, "You have traveled $totalDistance meters from your starting location.")
					
					lifecycleScope.launch {
						todaysDistance = Distance(today, totalDistance)
						distanceDao.insert_distance(todaysDistance)
						Log.d(TAG, "Updated database with ${todaysDistance.totalDistance} meters traveled on epoch day $today.")
					}
				}
				else
				{
					lastSavedLocation = location
					acquiredStartLocation = true
					Log.d(TAG, "Acquired starting location.")
				}
			}
		}

		/* Location request wants high accuracy results every 5 seconds. */
		locationRequest = LocationRequest.create().apply {
			priority = LocationRequest.PRIORITY_HIGH_ACCURACY
			interval = 5000  // milliseconds
		}

		Log.d(TAG, "Initialization complete.")
	}
	
	override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int
	{
		Log.d(TAG, "Starting the GPS service...")
		
		/* If we fail to subscribe, just stop running the service */
		if(!subscribe())
		{
			stopSelf(startId)
		}
		
		Log.d(TAG, "Startup complete.")
		return super.onStartCommand(intent, flags, startId)
	}
	
	override fun onDestroy() {
		super.onDestroy()
		Log.d(TAG, "Destroyed the GPS service.")
	}
	
	/* Initialize the database entry for the daily distance. */
	private fun initTodaysDistance()
	{
		distanceDao = UserDatabase.getInstance(this).distanceDao
		today = get_epoch_day()
		
		/* Make sure our data is initialized in the database. */
		if(distanceDao.distance_exists_now(today))
		{
			todaysDistance = distanceDao.get_distance_now(today)
			totalDistance = todaysDistance.totalDistance
			Log.d(TAG, "Initialized with $totalDistance saved meters traveled for epoch day $today.")
		}
		else
		{
			todaysDistance = Distance(today, 0f)
			distanceDao.insert_distance_now(todaysDistance)
			Log.d(TAG, "Initialized the distance traveled for epoch day $today.")
		}
	}

	/* Subscribe to GPS updates. */
	private fun subscribe(): Boolean
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
	private fun unsubscribe(): Boolean
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
