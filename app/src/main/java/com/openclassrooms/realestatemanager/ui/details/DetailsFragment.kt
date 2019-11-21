package com.openclassrooms.realestatemanager.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.ui.EstatesViewModel

class DetailsFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.details_fragment, container, false)
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getEstate()


    }

    private fun getEstate() {
        val args = arguments
        val id = args!!.getLong(KEY_ESTATE_FOR_DETAILS)
        val estatesViewModel: EstatesViewModel =
            ViewModelProviders.of(activity!!).get(EstatesViewModel::class.java)



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