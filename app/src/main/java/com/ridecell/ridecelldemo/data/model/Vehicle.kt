package com.ridecell.ridecelldemo.data.model

data class Vehicle(
    val id: String,
    val isActive: Boolean,
    val isAvailable: Boolean,
    val lat: Double,
    val lng: Double,
    val licencePlateNumber: String,
    val remainingMileage: Int,
    val remainingRangeInMeters: Int,
    val transmissionMode: String,
    val vehicleMake: String,
    val vehiclePickAbsoluteUrl: String,
    val vehicleType: String,
    val vehicleTypeId: Int
)
