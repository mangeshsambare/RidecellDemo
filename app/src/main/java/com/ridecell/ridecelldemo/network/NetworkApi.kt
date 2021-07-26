package com.ridecell.ridecelldemo.network

import com.google.gson.JsonObject
import com.ridecell.ridecelldemo.data.model.AuthenticationDto
import com.ridecell.ridecelldemo.data.model.PasswordRequirementsDto
import com.ridecell.ridecelldemo.data.model.PersonDto
import com.ridecell.ridecelldemo.data.model.Vehicle
import io.reactivex.rxjava3.core.Observable
import retrofit2.Response
import retrofit2.http.*
import java.util.*

interface NetworkApi {

    @POST("/api/v2/people/authenticate")
    fun login(@Body jsonObject: JsonObject?):
            Observable<Response<AuthenticationDto>>

    @POST("/api/v2/people/create")
    fun signupUser(@Body jsonObject: JsonObject?):
            Observable<Response<AuthenticationDto>>

    @GET("/api/v2/people/{personKey}")
    fun getUserDetails(
        @Header("Authorization") authorization: String?,
        @Path("personKey") key: String?):
            Observable<Response<PersonDto>>

    @POST("/api/v2/people/reset_password")
    fun resetPassword(@Body jsonObject: JsonObject?):
            Observable<Response<String>>

    @GET("/api/v2/people/password_requirements")
    fun getPasswordRequirements():
            Observable<Response<PasswordRequirementsDto>>

    @GET("/api/v2/vehicles")
    fun getVehicles(@Header("Authorization") authorization: String?):
            Observable<Response<ArrayList<Vehicle>>>
}