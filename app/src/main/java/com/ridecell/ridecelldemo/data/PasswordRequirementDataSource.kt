package com.ridecell.ridecelldemo.data

import com.ridecell.ridecelldemo.data.model.PasswordRequirementsDto
import com.ridecell.ridecelldemo.network.ApiCallback
import com.ridecell.ridecelldemo.network.NetworkClient
import com.ridecell.ridecelldemo.network.NetworkError
import io.reactivex.rxjava3.disposables.Disposable

class PasswordRequirementDataSource {

    // in-memory cache of the passwordRequirement object
    var passwordRequirement: PasswordRequirementsDto? = null
        private set

    fun getPasswordRequirements(){
        NetworkClient.instance.getPasswordRequirements(object : ApiCallback<PasswordRequirementsDto>{
            override fun requestSent() {
            }

            override fun subscribeRequest(disposable: Disposable) {
            }

            override fun success(t: PasswordRequirementsDto) {
                passwordRequirement = t
            }

            override fun error(error: NetworkError) {
            }

        })
    }
}