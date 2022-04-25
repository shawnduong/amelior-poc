package com.hci_g1.amelior

import android.app.Activity
import android.content.pm.PackageManager
import android.util.Log
import android.Manifest
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

object Permissions
{
	private const val TAG = "Permissions"
	private const val LOCATION_REQUEST_CODE = 10
	private const val ACTIVITY_RECOGNITION_REQUEST_CODE = 20

	/* Returns true if the permission is granted already. */
	private fun has_permissions(activity: Activity, permission: String): Boolean
	{
		return (ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED)
	}

	/* Request permissions if the user hasn't already accepted them. */
	fun request_permissions(activity: Activity, description: String, permission: String, code: Int)
	{
		/* Return early if permissions have already been granted. */
		if (has_permissions(activity, permission))
		{
			Log.d(TAG, "User has already accepted ${description} permissions.")
			return
		}

		/* Ask the user for permissions. */
		Log.d(TAG, "User has not accepted ${description} permissions. Asking now.")
		
		ActivityCompat.requestPermissions(
			activity,
			// FIXME: Hardcoded ACTIVITY_RECOGNITION permission request to test StepTracker.kt
//			arrayOf(Manifest.permission.ACTIVITY_RECOGNITION),
//			ACTIVITY_RECOGNITION_REQUEST_CODE
			
			// FIXME: Hardcoded ACCESS_FINE_LOCATION permission request to test Gps.kt
			arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
			LOCATION_REQUEST_CODE
			
			// FIXME: Misses some permissions,
			//  triggers "multiple permissions can't be requested at once" warning.
//			arrayOf(permission),
//			code
		)
	}

	/* Request all permissions. */
	fun request_all_permissions(activity: Activity)
	{
		request_permissions(
			activity,
			"location",
			Manifest.permission.ACCESS_FINE_LOCATION,
			LOCATION_REQUEST_CODE
		)

		request_permissions(
			activity,
			"activity recognition",
			Manifest.permission.ACTIVITY_RECOGNITION,
			ACTIVITY_RECOGNITION_REQUEST_CODE
		)
	}
}
