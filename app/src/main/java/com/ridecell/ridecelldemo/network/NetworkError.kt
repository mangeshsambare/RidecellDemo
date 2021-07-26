package com.ridecell.ridecelldemo.network

data class NetworkError(
    var code: Int? = 0,
    var error: String? = "Error",
    var message: String? = "Something might be wrong")
