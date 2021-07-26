package com.ridecell.ridecelldemo.data.model

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class RoleDto(
    val key: String? = null,
    val rank: Int? = 0
): Parcelable
