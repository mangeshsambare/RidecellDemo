package com.ridecell.ridecelldemo.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ridecell.ridecelldemo.data.LoginDataSource
import com.ridecell.ridecelldemo.data.PasswordRequirementDataSource
import com.ridecell.ridecelldemo.data.repository.LoginRepository
import com.ridecell.ridecelldemo.data.repository.PasswordRequirementRepository

/**
 * ViewModel provider factory to instantiate LoginViewModel.
 * Required given LoginViewModel has a non-empty constructor
 */
class LoginViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(
                loginRepository = LoginRepository(
                    dataSource = LoginDataSource()),
                passwordRequirementRepository = PasswordRequirementRepository(
                    dataSource = PasswordRequirementDataSource()
                )
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}