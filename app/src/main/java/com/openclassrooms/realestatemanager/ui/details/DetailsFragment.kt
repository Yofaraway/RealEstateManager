package com.openclassrooms.realestatemanager.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.DetailsFragmentBinding
import com.openclassrooms.realestatemanager.ui.EstatesViewModel
import com.openclassrooms.realestatemanager.ui.MainActivity
import kotlinx.android.synthetic.main.details_fragment.*

class DetailsFragment : Fragment() {

    // DATA BINDING & VIEW MODELS
    private lateinit var viewDataBinding: DetailsFragmentBinding
    private lateinit var estatesViewModel: EstatesViewModel
    private val detailsViewModel: DetailsViewModel by lazy {
        ViewModelProviders.of(this).get(DetailsViewModel::class.java)
    }

    // PHOTOS SLIDER
    val viewPager: ViewPager by lazy { details_images_slider_vp }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.details_fragment, container, false)

        // DATA BINDING
        viewDataBinding = DetailsFragmentBinding.bind(rootView).apply {
            this.viewmodel = detailsViewModel
        }
        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner

        (activity as MainActivity).supportActionBar?.hide()

        return viewDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        estatesViewModel = ViewModelProviders.of(activity!!).get(EstatesViewModel::class.java)
        getEstate()

    }


    private fun getEstate() {
        val args = arguments
        val id = args!!.getLong(KEY_ESTATE_FOR_DETAILS)

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