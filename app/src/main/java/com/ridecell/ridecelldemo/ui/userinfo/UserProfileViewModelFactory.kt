package com.ridecell.ridecelldemo.ui.userinfo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ridecell.ridecelldemo.data.LogoutDataSource
import com.ridecell.ridecelldemo.data.repository.LogoutRepository
import com.ridecell.ridecelldemo.data.UserProfileDataSource
import com.ridecell.ridecelldemo.data.repository.UserProfileRepository

class UserProfileViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserProfileViewModel::class.java)) {
            return UserProfileViewModel(
                LogoutRepository(LogoutDataSource()),
                UserProfileRepository(UserProfileDataSource())
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}