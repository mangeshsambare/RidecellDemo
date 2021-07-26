package com.ridecell.ridecelldemo.ui.login

/**
 * Authentication result : success (user details) or error message.
 */
data class LoginResult(
    val progress: Boolean? = false,
    val success: LoggedInUserView? = null,
    val error: Int? = null
)