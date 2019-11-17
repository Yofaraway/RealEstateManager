package com.openclassrooms.realestatemanager.ui.addestate

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.add_estate_fragment.*
import java.util.*


class AddEstateFragment : Fragment() {

    // VIEW
    lateinit var addEstateBtn: Button
    // Input field layouts
    lateinit var typeInput: TextInputLayout
    lateinit var surfaceInput: TextInputLayout
    lateinit var priceInput: TextInputLayout
    lateinit var roomsInput: TextInputLayout
    lateinit var bathroomsInput: TextInputLayout
    lateinit var bedroomsInput: TextInputLayout
    lateinit var addressInput: TextInputLayout
    lateinit var dateAvailableInput: TextInputLayout
    lateinit var dateSoldInput: TextInputLayout
    lateinit var agentInput:TextInputLayout
    lateinit var descriptionInput:TextInputLayout
    // Spinner
    lateinit var statusInput: Spinner
    var hasBeenSold: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            com.openclassrooms.realestatemanager.R.layout.add_estate_fragment,
            container,
            false
        )
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setViewComponents()
    }

    private fun setViewComponents() {
        // add the estate button
        addEstateBtn = add_the_estate_confirm_btn
        addEstateBtn.setOnClickListener { onAddEstateButtonClick() }

        // init input field layouts
        typeInput = add_estate_type
        surfaceInput = add_estate_surface
        priceInput = add_estate_price
        roomsInput = add_estate_rooms
        bathroomsInput = add_estate_bathrooms
        bedroomsInput = add_estate_bedrooms
        addressInput = add_estate_address
        agentInput = add_estate_agent
        descriptionInput = add_estate_description
        dateAvailableInput = add_estate_date_available
        dateSoldInput = add_estate_date_sold
        initDatePicker(dateAvailableInput.editText)
        initDatePicker(dateSoldInput.editText)

        // spinner
        statusInput = add_estate_status_spn
        val statusItems = ArrayList<String>()
        if (context != null) {
            statusItems.add(context!!.resources.getString(com.openclassrooms.realestatemanager.R.string.add_estate_status_available))
            statusItems.add(context!!.resources.getString(com.openclassrooms.realestatemanager.R.string.add_estate_status_sold))
            val adapter = ArrayAdapter(context!!, android.R.layout.simple_spinner_item, statusItems)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            statusInput.adapter = adapter
        }
        initStatusSpinnerListener()


    }

    private fun initDatePicker(editText: EditText?) {
        editText!!.inputType = InputType.TYPE_NULL
        editText.setOnClickListener {
            val cldr = Calendar.getInstance()
            val d = cldr.get(Calendar.DAY_OF_MONTH)
            val m = cldr.get(Calendar.MONTH)
            val y = cldr.get(Calendar.YEAR)
            // date picker dialog
            DatePickerDialog(
                context!!,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    val str = ("$dayOfMonth/$monthOfYear/$year")
                    editText.setText(str)
                }, y, m, d
            ).show()
        }
    }


    private fun initStatusSpinnerListener() {
        statusInput.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                // if available
                if (position == 0) {
                    dateSoldInput.visibility = View.GONE
                    hasBeenSold = false
                }
                // if sold
                else {
                    dateSoldInput.visibility = View.VISIBLE
                    hasBeenSold = true
                }
            }
        }
    }

    private fun onAddEstateButtonClick() {
        val inputs = mutableSetOf(
            typeInput,
            surfaceInput,
            priceInput,
            roomsInput,
            bathroomsInput,
            bedroomsInput,
            addressInput,
            dateAvailableInput,
            agentInput,
            descriptionInput
        )

        if (hasBeenSold) inputs.add(dateSoldInput)

        var isEmpty: Boolean
        val allInputs = mutableSetOf<Boolean>()

        for (field in inputs) {
            isEmpty = checkEmpty(field)
            allInputs.add(isEmpty)
        }

        // if there is no empty fields, we can create the new estate
        if (true !in allInputs) createNewEstate()
    }


    private fun checkEmpty(field: TextInputLayout): Boolean {
        val str: String = field.editText?.text.toString() + ""
        if (str == "") {
            showError(field)
            return true
        } else {
            field.isErrorEnabled = false
            return false
        }
    }

    private fun showError(field: TextInputLayout) {
        field.error =
            context?.resources?.getString(com.openclassrooms.realestatemanager.R.string.add_estate_missing_field)
    }

    private fun createNewEstate() {
        val type = typeInput.editText!!.text.toString()
        val price = priceInput.editText!!.text.toString().toInt()
        val surface = surfaceInput.editText!!.text.toString().toDouble()
        val rooms = roomsInput.editText!!.text.toString().toInt()
        val bathrooms = bathroomsInput.editText!!.text.toString().toInt()
        val bedrooms = bedroomsInput.editText!!.text.toString().toInt()
        val address = addressInput.editText!!.text.toString()
        val status = statusInput.selectedItem.toString()
        val dateAvailable = dateAvailableInput.editText!!.text.toString()
        val agent = dateAvailableInput.editText!!.text.toString()
        val description = descriptionInput.editText!!.text.toString()

        val pathPhotos = listOf<String>()
        val titlesPhotos = listOf<String>()
//
//        var estateToCreate = Estate(null,
//            type,
//            price,
//            surface,
//            rooms,
//            bathrooms,
//            bedrooms,
//            description,
//            pathPhotos,
//            titlesPhotos,
//            address,
//            status,
//            dateAvailable,
//            agent)


    }

    companion object {
        fun newInstance() = AddEstateFragment()
    }


}
