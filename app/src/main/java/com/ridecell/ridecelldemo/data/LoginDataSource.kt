package com.ridecell.ridecelldemo.data

import com.ridecell.ridecelldemo.data.model.AuthenticationDto
import com.ridecell.ridecelldemo.network.ApiCallback
import com.ridecell.ridecelldemo.network.NetworkClient
import com.ridecell.ridecelldemo.network.NetworkError
import io.reactivex.rxjava3.disposables.Disposable
import java.io.IOException
import java.util.*

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {

    val instance = NetworkClient.instance

    fun login(emailAddress: String, password: String, callback: ApiCallback<AuthenticationDto>)  {
        instance.login(emailAddress, password, callback)
    }

    fun logout() {
        // TODO: revoke authentication
    }
}