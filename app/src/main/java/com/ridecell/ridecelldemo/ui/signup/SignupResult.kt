package com.ridecell.ridecelldemo.ui.signup

data class SignupResult(
    val progress: Boolean? = false,
    val success: SignedUpUserView? = null,
    val error: Int? = null
)
