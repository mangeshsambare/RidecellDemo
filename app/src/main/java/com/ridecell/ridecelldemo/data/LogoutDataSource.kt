package com.ridecell.ridecelldemo.data

import com.ridecell.ridecelldemo.network.ApiCallback
import com.ridecell.ridecelldemo.network.NetworkClient
import java.io.IOException

class LogoutDataSource {

    fun logout(authorization: String, callback: ApiCallback<String>){
        NetworkClient.instance.logout(authorization, callback)
    }
}