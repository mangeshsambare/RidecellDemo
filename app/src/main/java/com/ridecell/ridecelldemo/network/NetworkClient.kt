package com.ridecell.ridecelldemo.network

import com.google.gson.JsonObject
import com.ridecell.ridecelldemo.data.model.AuthenticationDto
import com.ridecell.ridecelldemo.data.model.PasswordRequirementsDto
import com.ridecell.ridecelldemo.data.model.PersonDto
import com.ridecell.ridecelldemo.data.model.Vehicle
import java.util.*

class NetworkClient private constructor(): BaseClient() {

    private val networkApi: NetworkApi

    init {
        networkApi = retrofit.create(NetworkApi::class.java)
    }

    companion object {
        val instance = NetworkClient()

        private const val EMAIL = "email"
        private const val PASSWORD = "password"
        private const val DISPLAY_NAME = "display_name"
    }

    /* log in api*/
    fun login(email: String, password: String,
              callback: ApiCallback<AuthenticationDto>){

        val jsonObject = JsonObject()
        jsonObject.addProperty(EMAIL, email)
        jsonObject.addProperty(PASSWORD, password)

        val request = networkApi.login(jsonObject)

        makeRequest(request, callback)
    }

    /* signed up user*/
    fun signupUser(email: String, displayName: String,
                   password: String,
                   callback: ApiCallback<AuthenticationDto>){
        val jsonObject = JsonObject()
        jsonObject.addProperty(EMAIL, email)
        jsonObject.addProperty(DISPLAY_NAME, displayName)
        jsonObject.addProperty(PASSWORD, password)

        val request = networkApi.signupUser(jsonObject)

        makeRequest(request, callback)
    }

    /* get user details*/
    fun getUserDetails(authorization: String,
                       personKey: String,
                       callback: ApiCallback<PersonDto>){

        val request = networkApi.getUserDetails(authorization, personKey)

        makeRequest(request, callback)
    }

    /*reset password*/
    fun resetPassword(email: String, callback: ApiCallback<String>){
        val jsonObject = JsonObject()
        jsonObject.addProperty(EMAIL, email)

        val request = networkApi.resetPassword(jsonObject)

        makeRequest(request, callback)
    }

    /*get password requirements*/
    fun getPasswordRequirements(callback: ApiCallback<PasswordRequirementsDto>){

        val request = networkApi.getPasswordRequirements()

        makeRequest(request, callback)
    }

    /*get vehicles data*/
    fun getVehicles(authorization: String,
                    callback: ApiCallback<ArrayList<Vehicle>>){

        val request = networkApi.getVehicles(authorization)

        makeRequest(request, callback)
    }

    /*logout */
    fun logout(authorization: String,
               callback: ApiCallback<String>){
        callback.success("logout Successfully")
    }
}