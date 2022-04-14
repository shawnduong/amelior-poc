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
	private val LOCATION_REQUEST_CODE: Int = 10
	private val ACTIVITY_RECOGNITION_REQUEST_CODE: Int = 20

	/* Returns true if the permission is granted already. */
	private fun has_permissions(activity: Activity, permission: String): Boolean
	{
		return (ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED)
	}

	/* Request permissions and return true if the user either already has it or
	   if the user accepts the permissions request prompt. Else, return false. */
	fun request_permissions(activity: Activity, description: String, permission: String, code: Int): Boolean
	{
		/* Return true if permissions have already been granted. */
		if (has_permissions(activity, permission))
		{
			Log.d(TAG, "User has already accepted ${description} permissions.")
			return true
		}
		/* Ask the user for permissions. */
		else
		{
			Log.d(TAG, "User has not accepted ${description} permissions. Asking now.")

			ActivityCompat.requestPermissions(
				activity,
				arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
				code
			)

			if (has_permissions(activity, permission) == false)
			{
				Log.e(TAG, "User denied ${description} permissions.")
				return false
			}
			else
			{
				return true
			}
		}
	}

	/* Request all permissions and return true if all permissions are satisfied. */
	fun request_all_permissions(activity: Activity): Boolean
	{
		var allPermissionsGranted: Boolean = true

		allPermissionsGranted = allPermissionsGranted && request_permissions(
			activity,
			"location",
			Manifest.permission.ACCESS_FINE_LOCATION,
			LOCATION_REQUEST_CODE
		)

		allPermissionsGranted = allPermissionsGranted && request_permissions(
			activity,
			"activity recognition",
			Manifest.permission.ACTIVITY_RECOGNITION,
			ACTIVITY_RECOGNITION_REQUEST_CODE
		)

		if (allPermissionsGranted == true)
		{
			Log.d(TAG, "All permissions are accepted.")
			return true
		}

		Log.e(TAG, "One or more permissions were denied.")
		return false
	}
}
