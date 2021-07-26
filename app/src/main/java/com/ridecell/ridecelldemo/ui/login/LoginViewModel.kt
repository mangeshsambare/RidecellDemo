package com.ridecell.ridecelldemo.ui.login

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ridecell.ridecelldemo.R
import com.ridecell.ridecelldemo.data.repository.LoginRepository
import com.ridecell.ridecelldemo.data.model.AuthenticationDto
import com.ridecell.ridecelldemo.network.ApiCallback
import com.ridecell.ridecelldemo.network.NetworkError
import io.reactivex.rxjava3.disposables.Disposable
import java.util.regex.Matcher
import java.util.regex.Pattern


class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    fun login(emailAddress: String, password: String) {
        // can be launched in a separate asynchronous job
        loginRepository.login(emailAddress, password, object : ApiCallback<AuthenticationDto>{
            override fun requestSent() {
                _loginResult.value = LoginResult(progress = true)
            }

            override fun subscribeRequest(disposable: Disposable) {

            }

            override fun success(t: AuthenticationDto) {
                t.person?.emailAddress = emailAddress
                _loginResult.value =
                    LoginResult(progress = false, success = LoggedInUserView(authenticationDto = t))
            }

            override fun error(error: NetworkError) {
                _loginResult.value = LoginResult(progress = false, error = R.string.login_failed)
            }

        })
    }

    fun loginDataChanged(emailAddress: String, password: String) {
        if (!isEmailAddressValid(emailAddress)) {
            _loginForm.value = LoginFormState(emailAddressError = R.string.invalid_email_address)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    // A placeholder email address validation check
    private fun isEmailAddressValid(emailAddress: String): Boolean {
        return if (emailAddress.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()
        } else {
            emailAddress.isNotBlank()
        }
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        if (password.length > 5){
            val pattern: Pattern
            val matcher: Matcher
            val PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$"
            pattern = Pattern.compile(PASSWORD_PATTERN)
            matcher = pattern.matcher(password)

            return matcher.matches()
        }
        return false
    }
}