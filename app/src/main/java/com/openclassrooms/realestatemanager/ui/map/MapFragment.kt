package com.openclassrooms.realestatemanager.ui.map

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.model.Estate
import com.openclassrooms.realestatemanager.ui.EstatesViewModel
import com.openclassrooms.realestatemanager.ui.MainActivity
import com.openclassrooms.realestatemanager.ui.details.DetailsFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.map_fragment.*


class MapFragment : Fragment(), OnMapReadyCallback,
    GoogleMap.OnMarkerClickListener {

    private lateinit var map: GoogleMap
    private lateinit var estates: List<Estate>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as MainActivity).hideBottomNavigation(false)

        return inflater.inflate(
            R.layout.map_fragment,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // MAP
        if (map_view != null) {
            map_view.onCreate(null)
            map_view.onResume()
            map_view.getMapAsync(this)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        configureViewModel()
    }

    private fun configureViewModel() {
        val estatesViewModel: EstatesViewModel =
            ViewModelProviders.of(activity!!).get(EstatesViewModel::class.java)
        estatesViewModel.getEstates()
            .observe(viewLifecycleOwner, Observer<List<Estate>> {
                this.onListReceived(it)
            })
    }

    private fun onListReceived(estates: List<Estate>) {
        this.estates = estates
        createMarkers()
    }

    override fun onMapReady(p0: GoogleMap) {
        map = p0
        checkPermissions()
        map.setOnMarkerClickListener(this)

    }

    private fun createMarkers() {
        for (estate in estates) {
            if (!estate.latLng.isNullOrEmpty()) {
                val position = LatLng(estate.latLng[0]!!.toDouble(), estate.latLng[1]!!.toDouble())
                map.addMarker(MarkerOptions().position(position).title(estate.id.toString()))
            } else
                Log.d("MapFragment", "no LatLng")
        }

    }

    override fun onMarkerClick(p0: Marker?): Boolean {
        val id = p0!!.title.toLong()
        (activity as MainActivity).setFragment(DetailsFragment.newInstance(id), true)
        return true
    }


    private fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(activity!!, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        )
        // If permission already granted
            locationEnabled()
        else {
            // Asking for location permission
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                // Permission granted
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    locationEnabled()
                    // Permission not granted, let's ask again with some explanations
                } else {
                    Snackbar.make(view!!, R.string.map_permission_needed, Snackbar.LENGTH_SHORT)
                        .setAnchorView((activity as MainActivity).bottom_navigation)
                        .show()
                    // To ask after the snackbar has been displayed
                    Handler().postDelayed({
                        requestPermissions(
                            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                            LOCATION_PERMISSION_REQUEST_CODE
                        )
                    }, 2000)
                }
                return
            }
        }
    }

    private fun locationEnabled() {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context!!)
        map.isMyLocationEnabled = true
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            val currentLatLng = LatLng(location.latitude, location.longitude)
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12f))
        }
    }


    companion object {

        fun newInstance() = MapFragment()

        const val LOCATION_PERMISSION_REQUEST_CODE: Int = 17
    }
}
