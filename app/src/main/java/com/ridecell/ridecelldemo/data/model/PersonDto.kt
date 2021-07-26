package com.ridecell.ridecelldemo.data.model

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class PersonDto(
    val displayName: String? = null,
    val key: String? = null,
    var emailAddress: String? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null,
    val role: RoleDto? = null
): Parcelable
