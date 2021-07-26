package com.ridecell.ridecelldemo.data.repository

import com.ridecell.ridecelldemo.data.LoginDataSource
import com.ridecell.ridecelldemo.data.model.AuthenticationDto
import com.ridecell.ridecelldemo.network.ApiCallback

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class LoginRepository(val dataSource: LoginDataSource) {

    // in-memory cache of the loggedInUser object
    var user: AuthenticationDto? = null
        private set

    val isLoggedIn: Boolean
        get() = user != null

    init {
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
        user = null
    }

    fun logout() {
        user = null
        dataSource.logout()
    }

    fun login(emailAddress: String, password: String, callback: ApiCallback<AuthenticationDto>) {
        // handle login
        dataSource.login(emailAddress, password, callback)
    }
}