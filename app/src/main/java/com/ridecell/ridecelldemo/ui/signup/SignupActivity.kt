package com.ridecell.ridecelldemo.ui.signup

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ridecell.ridecelldemo.R
import com.ridecell.ridecelldemo.databinding.ActivitySignupBinding
import com.ridecell.ridecelldemo.ui.userinfo.UserInfoActivity

class SignupActivity: AppCompatActivity() {

    private lateinit var signupViewModel: SignupViewModel
    private lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val emailAddress = binding.emailAddress
        val fullName = binding.fullName
        val password = binding.password
        val confirmPassword = binding.confirmPassword
        val signup = binding.signup
        val loading = binding.loading

        signupViewModel = ViewModelProvider(this, SignupViewModelFactory())
            .get(SignupViewModel::class.java)

        signupViewModel.signupFormState.observe(this@SignupActivity, Observer {
            val signupState = it ?: return@Observer

            // disable signup button unless both email address / password is valid
            signup.isEnabled = signupState.isDataValid

            if (signupState.emailAddressError != null) {
                emailAddress.error = getString(signupState.emailAddressError)
            }
            if (signupState.fullNameError != null) {
                emailAddress.error = getString(signupState.fullNameError)
            }
            if (signupState.passwordError != null) {
                if (signupState.passwordError == R.string.password_not_matched){
                    confirmPassword.setError(getString(signupState.passwordError), null)
                } else {
                    password.setError(getString(signupState.passwordError), null)
                }
            }
        })

        signupViewModel.signupResult.observe(this@SignupActivity, Observer {
            val signupResult = it ?: return@Observer

            if (signupResult.progress!!){
                loading.visibility = View.VISIBLE
            } else {
                loading.visibility = View.GONE
                if (signupResult.error != null) {
                    showSignupFailed(signupResult.error)
                }
                if (signupResult.success != null) {
                    updateUiWithUser(signupResult.success)
                }
                setResult(Activity.RESULT_OK)
            }
        })

        emailAddress.afterTextChanged {
            signupViewModel.signupDataChanged(
                emailAddress.text.toString(),
                password.text.toString(),
                confirmPassword.text.toString())
        }

        password.apply {
            afterTextChanged {
                signupViewModel.signupDataChanged(
                    emailAddress.text.toString(),
                    password.text.toString(),
                    confirmPassword.text.toString())
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        signupViewModel.signup(
                            emailAddress.text.toString(),
                            fullName.text.toString(),
                            password.text.toString()
                        )
                }
                false
            }

            signup.setOnClickListener {
                loading.visibility = View.VISIBLE
                signupViewModel.signup(emailAddress.text.toString(),
                    fullName.text.toString(),
                    password.text.toString())
            }
        }
    }

    private fun updateUiWithUser(model: SignedUpUserView) {
        val welcome = getString(R.string.welcome)
        val displayName = model.authenticationDto.person?.displayName
        Toast.makeText(
            applicationContext,
            "$welcome $displayName",
            Toast.LENGTH_LONG
        ).show()

        val intent = Intent(this, UserInfoActivity::class.java)
        intent.putExtra("authentication", model.authenticationDto)
        startActivity(intent)
        finish()
    }

    private fun showSignupFailed(@StringRes errorString: Int) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }

    /**
     * Extension function to simplify setting an afterTextChanged action to EditText components.
     */
    fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
        this.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(editable: Editable?) {
                afterTextChanged.invoke(editable.toString())
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })
    }

}