package com.openclassrooms.realestatemanager.ui.addestate

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.openclassrooms.realestatemanager.databinding.AddEstateFragmentBinding
import java.util.*

class AddEstateFragment : Fragment() {


    private val viewModel: AddEstateViewModel by lazy {
        ViewModelProviders.of(this).get(AddEstateViewModel::class.java)
    }
    private lateinit var viewDataBinding: AddEstateFragmentBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(
            com.openclassrooms.realestatemanager.R.layout.add_estate_fragment,
            container,
            false
        )
        viewDataBinding = AddEstateFragmentBinding.bind(rootView).apply {
            this.viewmodel = viewModel
        }
        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner
        return viewDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.dateAvailableDatePicker.observe(this, Observer { displayDatePickerPopUp(false) })
        viewModel.dateSoldDatePicker.observe(this, Observer { displayDatePickerPopUp(true) })
    }

    private fun displayDatePickerPopUp(dateSold: Boolean) {
        val cldr = Calendar.getInstance()
        val d = cldr.get(Calendar.DAY_OF_MONTH)
        val m = cldr.get(Calendar.MONTH)
        val y = cldr.get(Calendar.YEAR)

        // date picker dialog
        DatePickerDialog(
            context!!,
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                val datePicked = ("$dayOfMonth/$monthOfYear/$year")
                if (!dateSold) viewModel.dateAvailable.value = datePicked
                else viewModel.dateSold.value = datePicked
            }, y, m, d
        ).show()
    }

    companion object {
        fun newInstance() = AddEstateFragment()
    }


}