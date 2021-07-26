package com.ridecell.ridecelldemo.ui.signup

/**
 * Data validation state of the signup form.
 */
data class SignupFormState(
    val emailAddressError: Int? = null,
    val fullNameError: Int? = null,
    val passwordError: Int? = null,
    val isDataValid: Boolean = false
)