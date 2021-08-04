
package com.ridecell.ridecelldemo.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import kotlinx.coroutines.*


class GpsReceiver(private val gpsCallBack: GpsCallBack) : BroadcastReceiver() {


    override fun onReceive(context: Context, intent: Intent) {
        GlobalScope.launch(Dispatchers.Main){
            delay(2000)
            if (PermissionUtils.isGpsEnable(context)){
                gpsCallBack.turnOn()
            } else {
                gpsCallBack.turnOff()
            }
            cancel()
        }
    }


    interface GpsCallBack{
        fun turnOn()

        fun turnOff()
    }
}