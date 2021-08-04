package com.ridecell.ridecelldemo.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

object PermissionUtils {

    const val REQUEST_ACCESS_LOCATION = 1
    const val REQUEST_ENABLE_GPS = 2

    // request access fine location & coarse location
    @RequiresApi(Build.VERSION_CODES.M)
    @JvmStatic
    fun requestAccessLocation(context: Context?, fragment: Fragment): Boolean {
        if (ContextCompat.checkSelfPermission(context!!,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(context,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                fragment.requestPermissions(arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), REQUEST_ACCESS_LOCATION)
            } else {
                return true
            }
        } else {
            if (ContextCompat.checkSelfPermission(context,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                fragment.requestPermissions(arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), REQUEST_ACCESS_LOCATION)
            } else {
                return true
            }
        }
        return false // here android will open default dialog for asking permission
    }

    /*check whether location permission is or not?*/
    fun isAccessLocation(context: Context?): Boolean{
        if (ContextCompat.checkSelfPermission(context!!,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true
        }
        return false
    }

    /* gps enable*/
    fun isGpsEnable(context: Context): Boolean{
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }
}