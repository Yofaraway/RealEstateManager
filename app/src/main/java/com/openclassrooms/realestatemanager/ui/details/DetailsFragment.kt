package com.openclassrooms.realestatemanager.ui.details

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.DetailsFragmentBinding
import com.openclassrooms.realestatemanager.ui.EstatesViewModel
import com.openclassrooms.realestatemanager.ui.MainActivity
import com.openclassrooms.realestatemanager.ui.update.UpdateEstateFragment
import com.openclassrooms.realestatemanager.utils.TAG_UPDATE_ESTATE_FRAGMENT
import kotlinx.android.synthetic.main.details_fragment.*


class DetailsFragment : Fragment(), OnMapReadyCallback {

    // DATA BINDING & VIEW MODELS
    private lateinit var viewDataBinding: DetailsFragmentBinding
    private lateinit var estatesViewModel: EstatesViewModel
    private val detailsViewModel: DetailsViewModel by lazy {
        ViewModelProviders.of(this).get(DetailsViewModel::class.java)
    }

    private lateinit var map: GoogleMap


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.details_fragment, container, false)

        setHasOptionsMenu(true)

        // DATA BINDING
        viewDataBinding = DetailsFragmentBinding.bind(rootView).apply {
            this.viewmodel = detailsViewModel
        }
        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner

        // MAP
        val mapFragment = viewDataBinding.detailsMapStatic
        mapFragment.onCreate(null)
        mapFragment.onResume()
        mapFragment.getMapAsync(this)
        return viewDataBinding.root
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        // Custom toolbar for the collapsing toolbar effect
        (activity as MainActivity).supportActionBar?.hide()
        (activity as MainActivity).hideBottomNavigation(true)

        val toolbar = details_toolbar
        toolbar.apply {
            setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
            setNavigationOnClickListener { fragmentManager?.popBackStack() }
            inflateMenu(R.menu.menu_edit)
            setOnMenuItemClickListener { item: MenuItem? -> onMenuItemClick(item) }
            title = context!!.resources.getString(R.string.app_name)
        }
        details_collapsing_toolbar.setExpandedTitleColor(Color.TRANSPARENT)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        estatesViewModel = ViewModelProviders.of(activity!!).get(EstatesViewModel::class.java)
        getEstate()
        detailsViewModel.hasBeenSold.observe(
            viewLifecycleOwner,
            Observer { t ->
                if (t) {
                    details_date_available_layout.visibility = View.GONE
                    details_date_sold_layout.visibility = View.VISIBLE
                } else {
                    details_date_sold_layout.visibility = View.GONE
                    details_date_available_layout.visibility = View.VISIBLE
                }
            })
    }


    private fun getEstate() {
        val id = arguments!!.getLong(KEY_ESTATE_FOR_DETAILS)

        estatesViewModel.getEstateWithId(id)
            .observe(this, Observer { t ->
                if (t != null) {
                    detailsViewModel.init(t, getCurrency()!!)
                    details_images_slider_vp.adapter =
                        PhotosSliderAdapter(
                            context!!,
                            detailsViewModel.estate.value!!.photosPathList,
                            detailsViewModel.estate.value!!.photosTitlesList
                        )
                    setNearPlaces()
                }
            })
    }

    private fun getCurrency(): String? {
        val prefs: SharedPreferences =
            context!!.getSharedPreferences("preferences", Context.MODE_PRIVATE)
        return prefs.getString("pref_currency", null)
    }


    private fun setNearPlaces() {
        details_near_to_layout.removeAllViews()
        if (detailsViewModel.estate.value!!.placesNear.isNullOrEmpty())
            details_near_to_icon.visibility = View.GONE
        else {
            details_near_to_icon.visibility = View.VISIBLE
            for (place in detailsViewModel.estate.value!!.placesNear) {
                val textView = TextView(context)
                textView.apply {
                    text = place
                }
                details_near_to_layout.addView(textView)
            }
        }
    }


    override fun onMapReady(p0: GoogleMap) {
        map = p0
        map.uiSettings.apply {
            isMapToolbarEnabled = false
        }
        getAddress()

    }

    private fun getAddress() {
        detailsViewModel.estate.observe(viewLifecycleOwner, Observer { t ->
            if (t != null) {
                if (!t.latLng.isNullOrEmpty()) {
                    details_no_marker_map.visibility = View.GONE
                    val position = LatLng(t.latLng[0]!!.toDouble(), t.latLng[1]!!.toDouble())
                    createMarkerForEstateAddress(position)
                } else
                    details_no_marker_map.visibility = View.VISIBLE
            }
        })
    }

    private fun createMarkerForEstateAddress(position: LatLng) {
        map.addMarker(MarkerOptions().position(position))
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 13.toFloat()))
    }

    private fun onMenuItemClick(item: MenuItem?): Boolean {
        val id = arguments!!.getLong(KEY_ESTATE_FOR_DETAILS)
        when (item?.itemId) {
            R.id.menu_toolbar_edit -> {
                (activity as MainActivity).setFragment(
                    UpdateEstateFragment.newInstance(id),
                    true, TAG_UPDATE_ESTATE_FRAGMENT
                )
                return true
            }
        }
        return false
    }


    companion object {
        const val KEY_ESTATE_FOR_DETAILS: String = "ID_ESTATE"

        fun newInstance(id: Long): DetailsFragment {
            val fragment = DetailsFragment()
            val args = Bundle()
            args.putLong(KEY_ESTATE_FOR_DETAILS, id)
            fragment.arguments = args
            return fragment
        }


    }

}