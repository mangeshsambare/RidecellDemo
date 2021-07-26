package com.ridecell.ridecelldemo.ui.login

import com.ridecell.ridecelldemo.data.model.AuthenticationDto

/**
 * User details post authentication that is exposed to the UI
 */
data class LoggedInUserView(
    val authenticationDto: AuthenticationDto
)