package com.apex.codeassesment.ui.location

import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.apex.codeassesment.R
import com.apex.codeassesment.databinding.ActivityLocationBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices


// TODO (Optional Bonus 8 points): Calculate distance between 2 coordinates using phone's location
class LocationActivity : AppCompatActivity() {
    companion object {
        private const val MILE_RATIO = 6.213712E-4f
    }

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    lateinit var userLocation: Location
    lateinit var binding: ActivityLocationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
            == android.content.pm.PackageManager.PERMISSION_GRANTED
        ) {
            setupLocationListener()
        } else {
            showLocationDialog()
        }

        val latitudeRandomUser = intent.getStringExtra("user-latitude-key")
        val longitudeRandomUser = intent.getStringExtra("user-longitude-key")

        val targetLocation = Location("")
        targetLocation.latitude = latitudeRandomUser?.toDouble() ?: 0.0
        targetLocation.longitude = longitudeRandomUser?.toDouble() ?: 0.0


        binding.locationRandomUser.text = getString(R.string.location_random_user, latitudeRandomUser, longitudeRandomUser)
        binding.locationCalculateButton.setOnClickListener {
            targetLocation.distanceTo(userLocation).let { distance ->
                binding.locationDistance.text = getString(R.string.location_result_miles, distance.toMile())
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun setupLocationListener() {
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            userLocation = location!!
            binding.locationPhone.text = getString(R.string.location_phone, userLocation.latitude.toString(), userLocation.longitude.toString())
        }
    }

    private fun showLocationDialog() {
        val alertDialog = AlertDialog.Builder(this)
            .setTitle("Location Permission")
            .setMessage("Please enable location permission to calculate distance")
            .setPositiveButton("OK") { dialog, _ ->
                requestPermissions(
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION),
                    1
                )
                dialog.dismiss()
            }
            .create()
        alertDialog.show()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if ((grantResults.isNotEmpty() && grantResults[0] == android.content.pm.PackageManager.PERMISSION_GRANTED)) {
            setupLocationListener()
        } else {
            showLocationDialog()
        }
    }

    fun Float.toMile(): Float {
        return this * MILE_RATIO
    }
}
