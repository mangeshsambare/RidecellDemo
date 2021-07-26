package com.ridecell.ridecelldemo.ui.userinfo

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ridecell.ridecelldemo.data.model.AuthenticationDto
import com.ridecell.ridecelldemo.data.model.PersonDto
import com.ridecell.ridecelldemo.databinding.FragmentUserProfileBinding
import com.ridecell.ridecelldemo.ui.login.LoginActivity
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.LocalDateTime
import java.util.*
import kotlin.math.abs

class UserProfileFragment: Fragment() {

    private lateinit var userProfileViewModel: UserProfileViewModel
    private lateinit var binding: FragmentUserProfileBinding

    private lateinit var activity: AppCompatActivity
    private lateinit var authenticationDto: AuthenticationDto

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as AppCompatActivity
        if (requireArguments() != null) {
            authenticationDto = requireArguments().getParcelable("authentication")!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userProfileViewModel = ViewModelProvider(this, UserProfileViewModelFactory())
            .get(UserProfileViewModel::class.java)

        userProfileViewModel.logoutResult.observe(this.viewLifecycleOwner, Observer {
            val logoutState = it ?: return@Observer
            if (logoutState.progress!!){
             binding.loading.visibility = View.VISIBLE
            } else {
                binding.loading.visibility = View.GONE
                if (logoutState.error != null) {
                    showError(logoutState.error)
                }
                if (logoutState.success) {
                    logoutUser()
                }
            }

        })

        userProfileViewModel.userDataResult.observe(this.viewLifecycleOwner, Observer {
            val getUserProfile = it ?: return@Observer
            if (getUserProfile.error != null){
                showError(getUserProfile.error)
            }
            if (getUserProfile.success != null){
                updateUserProfileData(getUserProfile.success.authenticationDto.person!!)
            }
        })

        userProfileViewModel.getUserProfile(authenticationDto)

        binding.logout.setOnClickListener {
            userProfileViewModel.logout(authenticationDto.authenticationToken!!)
        }
    }

    /*update UI*/
    private fun updateUserProfileData(personDto: PersonDto){
        binding.userFullName.text = personDto.displayName
        showDate(personDto.createdAt!!)
    }

    /* log out UI*/
    private fun logoutUser() {
        Toast.makeText(
            context,
            "Logged out",
            Toast.LENGTH_LONG
        ).show()
        val intent = Intent(activity, LoginActivity::class.java)
        activity.startActivity(intent)
        activity.finish()
    }

    private fun showError(@StringRes errorString: Int) {
        Toast.makeText(context, errorString, Toast.LENGTH_SHORT).show()
    }

    private fun showDate(dateTime: String){
        var dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH)
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")
        val createdDate: Date =
            dateFormat.parse(dateTime) //You will get date object relative to server/client timezone wherever it is parsed

        dateFormat.timeZone = TimeZone.getDefault()
        val formattedDate: String = dateFormat.format(createdDate)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val createdDate = LocalDateTime.parse(formattedDate)
            val todayDate = LocalDateTime.now()
            val days = Duration.between(createdDate, todayDate).toDays()
            binding.userProfileCreatedAt.text = days.toString()+" Days ago"
        } else {
            val df = DateFormat.getTimeInstance()
            df.timeZone = TimeZone.getTimeZone("gmt")
            val gmtTime = df.format(Date())
            val todayDate: Date =
                dateFormat.parse(gmtTime)
            val days: Long = createdDate.time - todayDate.time
            binding.userProfileCreatedAt.text = days.toString()+" Days ago"
        }
    }
}