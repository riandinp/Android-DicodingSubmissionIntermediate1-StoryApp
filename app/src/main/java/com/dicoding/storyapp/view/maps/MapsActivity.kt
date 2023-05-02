package com.dicoding.storyapp.view.maps

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.dicoding.storyapp.R
import com.dicoding.storyapp.data.remote.response.StoryItem
import com.dicoding.storyapp.databinding.ActivityMapsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import java.io.IOException
import java.util.Locale

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private var fragment: SupportMapFragment? = null

    private val mapsViewModel by viewModels<MapsViewModel>()
    private val boundsBuilder = LatLngBounds.Builder()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Story With Location"

        setupObserver()

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        fragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        fragment!!.getMapAsync(this)
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
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        setMapStyle()
    }

    private fun setMapStyle() {
        try {
            val success =
                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style))
            if (!success) {
                Log.e(TAG, "Style parsing failed.")
                Snackbar.make(binding.root, "Style parsing failed.", Snackbar.LENGTH_SHORT).show()
            }
        } catch (exception: Resources.NotFoundException) {
            Log.e(TAG, "Can't find style. Error: ", exception)
            Snackbar.make(
                binding.root,
                "Can't find style. Error: ${exception.message ?: ""}",
                Snackbar.LENGTH_SHORT
            ).show()
        }
    }

    private fun setupObserver() {
        mapsViewModel.getListStory()

        mapsViewModel.listStory.observe(this) {
            addStoryMarker(it)
        }

        mapsViewModel.loadingScreen.observe(this) {
            binding.pbLoadingScreen.isVisible = it
        }

        mapsViewModel.snackBarText.observe(this) {
            if (!it.contains("Success"))
                Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun addStoryMarker(listStory: List<StoryItem>) {
        listStory.forEach { story ->
            val latLng = LatLng(story.lat as Double, story.lon as Double)
            val addressName = getAddressName(story.lat, story.lon)
            mMap.addMarker(MarkerOptions().position(latLng).title(story.name).snippet(addressName ?:"Alamat tidak diketahui"))
            boundsBuilder.include(latLng)
        }

        val bounds: LatLngBounds = boundsBuilder.build()
        mMap.animateCamera(
            CameraUpdateFactory.newLatLngBounds(
                bounds,
                resources.displayMetrics.widthPixels,
                resources.displayMetrics.heightPixels,
                0
            )
        )
    }

    private fun getAddressName(lat: Double, lon: Double): String? {
        var addressName: String? = null
        val geocoder = Geocoder(this@MapsActivity, Locale.getDefault())
        try {
            @Suppress("DEPRECATION") val list = geocoder.getFromLocation(lat, lon, 1)
            if (list != null && list.size != 0) {
                addressName = list[0].getAddressLine(0)
                Log.d(TAG, "getAddressName: $addressName")
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return addressName
    }

    companion object {
        @JvmStatic
        fun start(context: Context) {
            val starter = Intent(context, MapsActivity::class.java)
            context.startActivity(starter)
        }

        private const val TAG = "MapsActivity"
    }
}