package com.ridecell.ridecelldemo.data

import com.ridecell.ridecelldemo.data.model.AuthenticationDto
import com.ridecell.ridecelldemo.network.ApiCallback
import com.ridecell.ridecelldemo.network.NetworkClient
import java.io.IOException
import java.util.*

/**
 * Class that handles signup and retrieves user information.
 */
class SignupDataSource {

    fun signup(emailAddress: String, fullName:String,
               password: String, callback: ApiCallback<AuthenticationDto>){
        NetworkClient.instance.signupUser(emailAddress, fullName,
            password, callback)
    }


}