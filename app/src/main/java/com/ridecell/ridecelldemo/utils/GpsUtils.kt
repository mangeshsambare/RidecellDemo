package com.ridecell.ridecelldemo.utils

import android.app.Activity
import android.content.Context
import android.content.IntentSender.SendIntentException
import android.location.LocationManager
import android.util.Log
import android.widget.Toast
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*

class GpsUtils(private val activity: Activity) {
    private val settingsClient: SettingsClient
    private val locationSettingsRequest: LocationSettingsRequest
    private val locationManager: LocationManager
    private val locationRequest: LocationRequest

    /*method for turn on GPS*/
    fun turnGpsOn(gpsCallBack: GpsReceiver.GpsCallBack) {
        if (PermissionUtils.isGpsEnable(activity)) {
            gpsCallBack.turnOn()
        } else {
            settingsClient.checkLocationSettings(locationSettingsRequest)
                    .addOnSuccessListener((activity)) {
                        gpsCallBack.turnOn()
                    }
                    .addOnFailureListener(activity) { e ->
                        val statusCode = (e as ApiException).statusCode
                        when (statusCode) {
                            LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
                                // Show the dialog by calling startResolutionForResult(), and check the
                                // result in onActivityResult().
                                val rae = e as ResolvableApiException
                                rae.startResolutionForResult(activity, PermissionUtils.REQUEST_ENABLE_GPS)
                            } catch (sie: SendIntentException) {
                                Log.d("GpsUtils", "PendingIntent unable to execute request.")
                            }
                            LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                                val errorMessage = "Location settings are inadequate, and cannot be " +
                                        "fixed here. Fix in Settings."
                                Log.e("GpsUtils", errorMessage)
                                gpsCallBack.turnOff()
                                Toast.makeText(activity, errorMessage, Toast.LENGTH_LONG).show()
                            }
                        }
                    }
        }
    }

    init {
        locationManager = activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        settingsClient = LocationServices.getSettingsClient(activity)
        locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 10 * 1000.toLong()
        locationRequest.fastestInterval = 2 * 1000.toLong()
        val builder = LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest)
        locationSettingsRequest = builder.build()
    }


}