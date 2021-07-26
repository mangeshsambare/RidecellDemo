package com.ridecell.ridecelldemo.ui.login

/**
 * Data validation state of the login form.
 */
data class LoginFormState(
    val emailAddressError: Int? = null,
    val passwordError: Int? = null,
    val isDataValid: Boolean = false
)