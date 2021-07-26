package com.ridecell.ridecelldemo

import androidx.multidex.MultiDexApplication
import com.ridecell.ridecelldemo.network.NetworkRepository

/**
 * Controls application context from this class
 */
class ApplicationController:  MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        // can initialize firebase, or other services here
        initializeNetworkRepository()
    }

    /*initialize Network Repository*/
    private fun initializeNetworkRepository(){
        NetworkRepository.instance.initialize(this)
    }
}