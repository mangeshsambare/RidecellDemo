package com.ridecell.ridecelldemo.network

import android.content.Context

class NetworkRepository private constructor() {

    private var appConfiguration: AppConfiguration? = null

    var baseUrl: String? = null

    companion object{
        val instance = NetworkRepository()
    }

    /*initialize object*/
    fun initialize(context: Context){

        appConfiguration = AppConfiguration(context)
        if (appConfiguration != null){
            baseUrl = appConfiguration!!.getBaseUrl()

        } else{
            throw RuntimeException("app configuration is not initialized")
        }
    }
}