package com.ridecell.ridecelldemo.ui.userinfo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ridecell.ridecelldemo.R
import com.ridecell.ridecelldemo.data.repository.LogoutRepository
import com.ridecell.ridecelldemo.data.repository.UserProfileRepository
import com.ridecell.ridecelldemo.data.model.AuthenticationDto
import com.ridecell.ridecelldemo.data.model.PersonDto
import com.ridecell.ridecelldemo.network.ApiCallback
import com.ridecell.ridecelldemo.network.NetworkError
import com.ridecell.ridecelldemo.ui.login.LoggedInUserView
import io.reactivex.rxjava3.disposables.Disposable

class UserProfileViewModel(private val logoutRepository: LogoutRepository,
                           private val userProfileRepository: UserProfileRepository
): ViewModel() {

    private val _logoutResult = MutableLiveData<LogoutResult>()
    val logoutResult: LiveData<LogoutResult> = _logoutResult

    private val _userDataResult = MutableLiveData<UserProfileResult>()
    val userDataResult: LiveData<UserProfileResult> = _userDataResult

    fun logout(authorization: String){
        logoutRepository.logout(authorization, object : ApiCallback<String>{
            override fun requestSent() {
                _logoutResult.value =
                    LogoutResult(progress = true)
            }

            override fun subscribeRequest(disposable: Disposable) {
            }

            override fun success(t: String) {
                _logoutResult.value =
                    LogoutResult(progress = false, success = true)
            }

            override fun error(error: NetworkError) {
                _logoutResult.value = LogoutResult(progress = false, error = R.string.logout_failed)
            }

        })
    }

    fun getUserProfile(authenticationDto: AuthenticationDto){
        userProfileRepository.getUserProfile(authenticationDto.authenticationToken!!,
            authenticationDto.person?.key!!, object : ApiCallback<PersonDto> {
            override fun requestSent() {
                _userDataResult.value = UserProfileResult(progress = true)
            }

            override fun subscribeRequest(disposable: Disposable) {
            }

            override fun success(t: PersonDto) {
                authenticationDto.person = t
                _userDataResult.value = UserProfileResult(progress = false, LoggedInUserView(authenticationDto)                                    )
            }

            override fun error(error: NetworkError) {
                _userDataResult.value = UserProfileResult(progress = false, error = R.string.user_profile_failed)
            }

        })
    }
}