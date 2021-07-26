package com.ridecell.ridecelldemo.ui.signup

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ridecell.ridecelldemo.R
import com.ridecell.ridecelldemo.data.repository.SignupRepository
import com.ridecell.ridecelldemo.data.model.AuthenticationDto
import com.ridecell.ridecelldemo.network.ApiCallback
import com.ridecell.ridecelldemo.network.NetworkError
import io.reactivex.rxjava3.disposables.Disposable
import java.util.regex.Matcher
import java.util.regex.Pattern

class SignupViewModel(private val signupRepository: SignupRepository): ViewModel() {

    private val _signupForm = MutableLiveData<SignupFormState>()
    val signupFormState: LiveData<SignupFormState> = _signupForm

    private val _signupResult = MutableLiveData<SignupResult>()
    val signupResult: LiveData<SignupResult> = _signupResult

    fun signup(emailAddress: String, fullName: String, password: String) {
        // can be launched in a separate asynchronous job
        val result = signupRepository.signup(emailAddress, fullName,
            password, object : ApiCallback<AuthenticationDto>{
                override fun requestSent() {
                    _signupResult.value =
                        SignupResult(progress = true)
                }

                override fun subscribeRequest(disposable: Disposable) {
                }

                override fun success(t: AuthenticationDto) {
                    _signupResult.value =
                        SignupResult(progress = false,
                            success = SignedUpUserView(t)
                        )
                }

                override fun error(error: NetworkError) {
                    _signupResult.value = SignupResult(progress = false, error = R.string.signup_failed)
                }

            })

    }

    fun signupDataChanged(emailAddress: String, password: String,
                          confirmPassword: String) {
        if (!isEmailAddressValid(emailAddress)) {
            _signupForm.value = SignupFormState(emailAddressError = R.string.invalid_email_address)
        } else if (!isPasswordValid(password)) {
            _signupForm.value = SignupFormState(passwordError = R.string.invalid_password)
        } else if (password != confirmPassword) {
            _signupForm.value = SignupFormState(passwordError = R.string.password_not_matched)
        }
        else {
            _signupForm.value = SignupFormState(isDataValid = true)
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