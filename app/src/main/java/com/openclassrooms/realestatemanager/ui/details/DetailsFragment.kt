package com.openclassrooms.realestatemanager.ui.details

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
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
    // PHOTOS SLIDER
    private val viewPager: ViewPager by lazy { details_images_slider_vp }

    private lateinit var map: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.details_fragment, container, false)
        (activity as MainActivity).hideBottomNavigation(true)

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


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHasOptionsMenu(true)
        estatesViewModel = ViewModelProviders.of(activity!!).get(EstatesViewModel::class.java)
        getEstate()

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        // Replace icon in toolbar
        menu.clear()
        inflater.inflate(R.menu.menu_edit, menu)
        val actionBar = (activity as MainActivity).supportActionBar
        actionBar?.apply {
            // back button
            setDisplayHomeAsUpEnabled(true)
            // title
            title =
                context!!.resources.getString(R.string.app_name)
        }
    }

    private fun getEstate() {
        val id = arguments!!.getLong(KEY_ESTATE_FOR_DETAILS)

        estatesViewModel.getEstateWithId(id)
            .observe(this, Observer { t ->
                if (t != null) {
                    detailsViewModel.init(t)
                    viewPager.adapter =
                        PhotosSliderAdapter(
                            context!!,
                            detailsViewModel.estate.value!!.pathPhotos,
                            detailsViewModel.estate.value!!.titlesPhotos
                        )
                }
            })
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


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = arguments!!.getLong(KEY_ESTATE_FOR_DETAILS)
        when (item.itemId) {
            R.id.menu_toolbar_edit -> {
                (activity as MainActivity).setFragment(
                    UpdateEstateFragment.newInstance(id),
                    true, TAG_UPDATE_ESTATE_FRAGMENT
                )
                return true
            }
            android.R.id.home -> {
                fragmentManager?.popBackStack()
                return true
            }
            else -> super.onOptionsItemSelected(item)
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