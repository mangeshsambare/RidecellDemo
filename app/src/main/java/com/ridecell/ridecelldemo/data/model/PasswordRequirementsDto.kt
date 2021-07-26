package com.ridecell.ridecelldemo.data.model

data class PasswordRequirementsDto(
    val requireLowerCase: Boolean? = false,
    val last_x_passwords: Int? = 0,
    val requireNumber: Boolean? = false,
    val requireSpecial: Boolean? = false,
    val requireUppercase: Boolean? = false,
    val maxChars: Int? = 0,
    val minChars: Int? = 0,
)
