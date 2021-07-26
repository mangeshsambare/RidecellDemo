package com.ridecell.ridecelldemo.data.model

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class AuthenticationDto(
    val authenticationToken: String? = null,
    var person: PersonDto? = null): Parcelable
