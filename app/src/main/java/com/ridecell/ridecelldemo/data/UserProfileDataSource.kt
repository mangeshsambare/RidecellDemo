package com.ridecell.ridecelldemo.data

import com.ridecell.ridecelldemo.data.model.AuthenticationDto
import com.ridecell.ridecelldemo.data.model.PersonDto
import com.ridecell.ridecelldemo.network.ApiCallback
import com.ridecell.ridecelldemo.network.NetworkClient

class UserProfileDataSource {

    fun getUserProfile(authorization: String, personKey: String,
                       callback: ApiCallback<PersonDto>){
        NetworkClient.instance.getUserDetails(authorization, personKey, callback)
    }
}