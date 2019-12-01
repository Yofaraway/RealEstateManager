package com.openclassrooms.realestatemanager.ui.update

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.chip.Chip
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.UpdateEstateFragmentBinding
import com.openclassrooms.realestatemanager.photos.*
import com.openclassrooms.realestatemanager.ui.EstatesViewModel
import com.openclassrooms.realestatemanager.ui.MainActivity
import com.openclassrooms.realestatemanager.ui.addestate.AddEstateFragment
import com.openclassrooms.realestatemanager.ui.details.DetailsFragment
import kotlinx.android.synthetic.main.update_estate_fragment.*
import java.io.File
import java.util.*

// Same class and layout as AddEstateFragment, except we start by loading an estate (and all its values) in the viewmodel

class UpdateEstateFragment : Fragment() {

    // VIEW MODELS & DATA BINDING
    private lateinit var viewDataBinding: UpdateEstateFragmentBinding
    private val viewModel: UpdateEstateViewModel by lazy {
        ViewModelProviders.of(this).get(UpdateEstateViewModel::class.java)
    }
    private val estatesViewModel: EstatesViewModel by lazy {
        ViewModelProviders.of(activity!!).get(EstatesViewModel::class.java)
    }


    // NEAR PLACES
    private val placesChoices by lazy { context!!.resources.getStringArray(R.array.add_estate_near_choices) }
    // PHOTOS
    private var holdersPhoto = mutableListOf<ConstraintLayout?>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.update_estate_fragment, container, false)
        (activity as MainActivity).hideBottomNavigation(true)

        // DATA BINDING
        viewDataBinding =
            UpdateEstateFragmentBinding.bind(rootView).apply { this.viewmodel = viewModel }
        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner

        return viewDataBinding.root
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        // Replace icon in toolbar
        menu.clear()
        val actionBar = (activity as MainActivity).supportActionBar
        actionBar?.apply {
            // back button
            setDisplayHomeAsUpEnabled(true)
            // title
            title =
                context!!.resources.getString(R.string.update_estate_title)
        }
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHasOptionsMenu(true)

        // Get estate and init the viewmodel with all the values of the estate
        getEstate()
        // Get near places
        viewModel.nearPlaces.observe(viewLifecycleOwner, Observer { t -> addChipsToView(t!!) })
        // Load photos
        viewModel.nearPlaces.observe(viewLifecycleOwner, Observer { t ->
            run {
                if (t.isNullOrEmpty()) viewModel.atLeastOnePhoto.value = false
                else {
                    viewModel.atLeastOnePhoto.value = true
                    addPhotosToView(t)
                }
            }
        })
        // Listeners
        setOnClickListeners()
    }


    private fun getEstate() {
        val args = arguments
        val id = args!!.getLong(DetailsFragment.KEY_ESTATE_FOR_DETAILS)

        estatesViewModel.getEstateWithId(id).observe(viewLifecycleOwner, Observer { t ->
            viewModel.init(
                t,
                context!!.resources.getString(R.string.add_estate_status_available),
                context!!.resources.getString(R.string.add_estate_status_sold),
                placesChoices
            )
        })
    }


    private fun setOnClickListeners() {
        // --- DATE PICKERS --- //
        // date available
        viewDataBinding.updateEstateDateAvailable.editText?.setOnClickListener {
            displayDatePickerPopUp(
                false
            )
        }
        // date sold
        viewDataBinding.updateEstateDateSold.editText?.setOnClickListener {
            displayDatePickerPopUp(
                true
            )
        }

        // -- NEARS PLACES -- //
        viewDataBinding.updateEstateNear.setOnClickListener { showNearChoicesDialog() }

        // -- STATUS -- //
        // -- PHONE BUTTONS -- //
        // camera
        viewDataBinding.updateEstateLoadFromCameraBtn.setOnClickListener { displayCameraIntent() }
        // gallery
        viewDataBinding.updateEstateLoadFromGalleryBtn.setOnClickListener { displayGalleryIntent() }


    }


    /** DATE PICKERS **/
    private fun displayDatePickerPopUp(dateSold: Boolean) {
        val cldr = Calendar.getInstance()
        DatePickerDialog(
            context!!,
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                val datePicked = Calendar.getInstance()
                datePicked.set(year, monthOfYear, dayOfMonth)
                if (!dateSold) {
                    viewModel.dateAvailable.value = datePicked.time
                } else {
                    viewModel.dateSold.value = datePicked.time
                }
            }, cldr.get(Calendar.YEAR), cldr.get(Calendar.MONTH), cldr.get(Calendar.DAY_OF_MONTH)
        ).show()
    }


    /** NEAR PLACES (CHECKBOXES & CHIPS) **/
    private fun showNearChoicesDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(context!!)
        builder.apply {
            setPositiveButton("OK") { _, _ -> updateNearList() }
            setCancelable(true)
            setMultiChoiceItems(
                placesChoices, viewModel.placesChoicesCheckedList
            ) { _, which, isChecked -> viewModel.placesChoicesCheckedList[which] = isChecked }
            create()
            show()
        }
    }

    private fun updateNearList() {
        val list: MutableList<String> = mutableListOf()
        for ((index, b) in viewModel.placesChoicesCheckedList.withIndex()) {
            if (b) list.add(placesChoices[index])
        }
        viewModel.nearPlaces.value = list
    }

    private fun addChipsToView(list: List<String?>) {
        update_estate_near_chips_box.removeAllViews()
        for (place in list) {
            val chip =
                layoutInflater.inflate(
                    R.layout.chip_layout,
                    update_estate_near_chips_box,
                    false
                ) as Chip
            chip.text = place
            chip.setOnClickListener { showNearChoicesDialog() }
            update_estate_near_chips_box.addView(chip)
        }
    }


    /** PHOTO BUTTONS **/
    private fun displayCameraIntent() {
        val newPhoto: File = createFile(context!!)
        viewModel.newPhotoPath = newPhoto.absolutePath
        val camera: Intent? = getCameraIntent(context!!, newPhoto)
        startActivityForResult(camera, AddEstateFragment.REQUEST_IMAGE_CAPTURE)
    }

    private fun displayGalleryIntent() {
        startActivityForResult(getGalleryIntent(), AddEstateFragment.REQUEST_GALLERY)
    }


    private fun addPhotosToView(listPaths: List<String?>) {
        holdersPhoto = mutableListOf()
        val listTitles = viewModel.estate?.titlesPhotos
        val test = viewModel.estate?.pathPhotos
        for ((index, path) in test!!.withIndex()) {
            // photoHolder contains the image, the title and the delete button
            val photoHolder: ConstraintLayout? = getLayout(context!!, index)

            // ImageView
            val imageView = getImageViewFromContentURI(context!!, Uri.parse(path))

            // EditText
            val editText = getEditText(context!!, index, listTitles!![index])
            editText?.addTextChangedListener(onTitlePhotoChanged(index))

            // Button
            val deleteBtn = getDeleteButton(context!!, index)
            deleteBtn?.setOnClickListener { onDeletePhotoBtnClicked(index) }

            photoHolder?.addView(imageView)
            photoHolder?.addView(editText)
            photoHolder?.addView(deleteBtn)
            holdersPhoto.add(photoHolder)
            viewDataBinding.updateEstatePhotosLayout.addView(photoHolder)

        }
    }

    private fun onTitlePhotoChanged(index: Int): TextWatcher {
        return object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.toString() == "") viewModel.photoTitleList.value!![index] =
                    context!!.resources.getString(R.string.add_estate_no_title)
                else viewModel.photoTitleList.value!![index] = s.toString()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }
    }

    private fun onDeletePhotoBtnClicked(index: Int) {
        holdersPhoto[index]?.visibility = View.GONE
        viewModel.photoTitleList.value?.removeAt(index)
        viewModel.photoPathList.value?.removeAt(index)

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                fragmentManager?.popBackStack()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
        return false
    }


    companion object {
        private const val KEY_ESTATE_FOR_UPDATE: String = "ID_ESTATE"

        fun newInstance(id: Long): UpdateEstateFragment {
            val fragment = UpdateEstateFragment()
            val args = Bundle()
            args.putLong(KEY_ESTATE_FOR_UPDATE, id)
            fragment.arguments = args
            return fragment
        }
    }

}
