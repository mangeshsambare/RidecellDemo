package com.ridecell.ridecelldemo.ui.userinfo

data class LogoutResult(
    val progress: Boolean? = false,
    val success: Boolean = false,
    val error: Int? = null
)
