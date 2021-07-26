package com.ridecell.ridecelldemo.ui.userinfo

import com.ridecell.ridecelldemo.ui.login.LoggedInUserView

data class UserProfileResult(
    val progress: Boolean? = false,
    val success: LoggedInUserView? = null,
    val error: Int? = null
)
