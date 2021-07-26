package com.ridecell.ridecelldemo.data.repository

import com.ridecell.ridecelldemo.data.UserProfileDataSource
import com.ridecell.ridecelldemo.data.model.PersonDto
import com.ridecell.ridecelldemo.network.ApiCallback

class UserProfileRepository(val dataSource: UserProfileDataSource) {

    fun getUserProfile(authorization: String, personKey: String,
                       callback: ApiCallback<PersonDto>){
        dataSource.getUserProfile(authorization, personKey, callback)
    }

}