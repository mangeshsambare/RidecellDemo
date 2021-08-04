package com.ridecell.ridecelldemo.data.repository

import com.ridecell.ridecelldemo.data.LogoutDataSource
import com.ridecell.ridecelldemo.network.ApiCallback

class LogoutRepository(val dataSource: LogoutDataSource) {

    fun logout(authorization: String, callback: ApiCallback<String>){
        dataSource.logout(authorization, callback)
    }
}