package com.ridecell.ridecelldemo.data.repository

import com.ridecell.ridecelldemo.data.SignupDataSource
import com.ridecell.ridecelldemo.data.model.AuthenticationDto
import com.ridecell.ridecelldemo.network.ApiCallback

class SignupRepository(val dataSource: SignupDataSource) {


    // in-memory cache of the signedUpUser object
    var user: AuthenticationDto? = null
        private set

    val isSignedUp: Boolean
        get() = user != null

    fun signup(emailAddress: String, fullName: String,
               password: String, callback: ApiCallback<AuthenticationDto>){
        // handle signup
       dataSource.signup(emailAddress, fullName, password, callback)
    }

}