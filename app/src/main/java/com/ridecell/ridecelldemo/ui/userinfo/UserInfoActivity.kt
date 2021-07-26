package com.ridecell.ridecelldemo.ui.userinfo

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.navigation.NavigationBarView
import com.ridecell.ridecelldemo.R
import com.ridecell.ridecelldemo.data.model.AuthenticationDto
import com.ridecell.ridecelldemo.databinding.ActivityUserInfoBinding

class UserInfoActivity: AppCompatActivity() {

    private lateinit var binding: ActivityUserInfoBinding

    private lateinit var authenticationDto: AuthenticationDto


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent != null){
            authenticationDto = intent.getParcelableExtra("authentication")!!
        }

        val bottomNavigationView = binding.bottomNavigation
        bottomNavigationView.selectedItemId = R.id.page_map
        showMap()
        bottomNavigationView.setOnItemSelectedListener(object :
            NavigationBarView.OnItemSelectedListener {
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                if (item == null){
                    return false
                }
                if (bottomNavigationView.selectedItemId == item.itemId){
                    return false
                }
                when(item.itemId){
                    R.id.page_map ->{
                        showMap()
                    }
                    R.id.page_user ->{
                        showUserProfile()
                    }
                }
                return true
            }

        })
    }

    /*show map*/
    private fun showMap(){
        val fragment = MapFragment()
        val arguments = Bundle()
        arguments.putParcelable("authentication", authenticationDto)
        fragment.arguments = arguments
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, fragment)
            .commit()
    }


    /*show user profile*/
    private fun showUserProfile(){
        val fragment = UserProfileFragment()
        val arguments = Bundle()
        arguments.putParcelable("authentication", authenticationDto)
        fragment.arguments = arguments
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, fragment)
            .commit()

    }
}