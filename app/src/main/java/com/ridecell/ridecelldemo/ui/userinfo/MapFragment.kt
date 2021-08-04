package com.ridecell.ridecelldemo.ui.userinfo

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Criteria
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.ui.IconGenerator
import com.ridecell.ridecelldemo.data.model.AuthenticationDto
import com.ridecell.ridecelldemo.databinding.FragmentMapBinding
import com.ridecell.ridecelldemo.utils.GpsReceiver
import com.ridecell.ridecelldemo.utils.GpsUtils
import com.ridecell.ridecelldemo.utils.PermissionUtils
import kotlinx.coroutines.*


class MapFragment: Fragment(), OnMapReadyCallback, GpsReceiver.GpsCallBack, LocationListener {

    private lateinit var binding: FragmentMapBinding
    private lateinit var map: GoogleMap
    private lateinit var activity: AppCompatActivity

    private var gpsReceiver: GpsReceiver? = null

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
        binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapView = binding.mapView
        mapView.onCreate(savedInstanceState)

        grantPermission()

        mapView.onResume() // needed to get the map to display immediately

    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        unregisterReceiver()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mapView.onDestroy()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PermissionUtils.REQUEST_ACCESS_LOCATION -> if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                try {
                    val gpsEnable = PermissionUtils.isGpsEnable(activity)
                    if (!gpsEnable) {
                        GpsUtils(activity).turnGpsOn(this)
                        registerReceiver()
                    } else {
                        getUsersCurrentLocation()
                    }
                    MapsInitializer.initialize(activity)
                }catch (e: Exception){
                    e.printStackTrace()
                }

                binding.mapView.getMapAsync(this)
            } else {
                Toast.makeText(activity, "Request Permission is not granted", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            PermissionUtils.REQUEST_ENABLE_GPS -> if (resultCode == Activity.RESULT_OK) {
                grantPermission()
            } else {

            }
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        if (ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        map.isMyLocationEnabled = true
        addMarkers()
        val gpsEnable = PermissionUtils.isGpsEnable(activity)
        if (!gpsEnable) {
            GpsUtils(activity).turnGpsOn(this)
            registerReceiver()
        } else {
            getUsersCurrentLocation()
        }
    }

    override fun turnOn() {
        getUsersCurrentLocation()
    }

    override fun turnOff() {
        Toast.makeText(activity,
            "For your current location, GPS must ON",
            Toast.LENGTH_SHORT).show()
    }

    override fun onLocationChanged(location: Location) {
        updateUserLocation(location)
    }

    private fun addMarkers(){
        val iconFactory = IconGenerator(activity)

        val sydney = LatLng(-34.0, 151.0)
        addIcon(iconFactory, "Sydney", sydney)


        val delhi = LatLng(28.70, 77.10)
        iconFactory.setColor(Color.CYAN);
        addIcon(iconFactory, "Delhi", delhi)

        val Mumbai = LatLng(19.07, 72.87)
        iconFactory.setStyle(IconGenerator.STYLE_RED);
        addIcon(iconFactory, "Mumbai", Mumbai)

        val Kolkata = LatLng(22.57, 88.36)
        iconFactory.setStyle(IconGenerator.STYLE_ORANGE);
        addIcon(iconFactory, "Kolkata", Kolkata)

        val Bangalore = LatLng(12.97, 77.59)
        iconFactory.setStyle(IconGenerator.STYLE_GREEN);
        addIcon(iconFactory, "Bangalore", Bangalore)

        map.setOnMarkerClickListener { marker ->
            val markerLocation = CameraUpdateFactory.newLatLngZoom(marker.position, 10f)
            map.animateCamera(markerLocation)
            marker.showInfoWindow()
            true
        }
    }

    @SuppressLint("MissingPermission")
    private fun getUsersCurrentLocation(){
        GlobalScope.launch(Dispatchers.Main){
            delay(2000)
            val locationManager = activity.getSystemService(LOCATION_SERVICE) as LocationManager?
            val criteria = Criteria()
            val provider = locationManager!!.getBestProvider(criteria, true)
            val location: Location? = locationManager.getLastKnownLocation(provider!!)

            if (location != null) {
               updateUserLocation(location)
            } else {
                locationManager.requestLocationUpdates(
                    LocationManager.PASSIVE_PROVIDER, 0, 200f, this@MapFragment)
            }
            cancel()
        }
    }

    private fun addIcon(iconFactory: IconGenerator, text: String, position: LatLng) {
        val markerOptions =
            MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon()))
                .position(position).anchor(iconFactory.anchorU, iconFactory.anchorV)
                .title(text)
        map.addMarker(markerOptions)
    }

    private fun grantPermission(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (PermissionUtils.requestAccessLocation(activity, this)){
                MapsInitializer.initialize(activity)
                binding.mapView.getMapAsync(this)
            }
        } else {
            // Check permission below sdk M
            TODO("VERSION.SDK_INT < M")
        }
    }

    /*register gps enable receiver*/
    private fun registerReceiver(){
        gpsReceiver = GpsReceiver(this)
        val intentFilter = IntentFilter(LocationManager.MODE_CHANGED_ACTION)
        activity.registerReceiver(gpsReceiver, intentFilter)
    }

    /*unregister gps enable*/
    private fun unregisterReceiver(){
        if (gpsReceiver != null)
        {
            activity.unregisterReceiver(gpsReceiver)
            gpsReceiver = null
        }
    }

    private fun updateUserLocation(location: Location){
        val latitude = location.latitude
        val longitude = location.longitude
        val latLng = LatLng(latitude, longitude)
        val coordinate = LatLng(latitude, longitude)
        val userLocation = CameraUpdateFactory.newLatLngZoom(coordinate, 10f)
        map.animateCamera(userLocation)
    }



}