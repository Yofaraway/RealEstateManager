package com.openclassrooms.realestatemanager.ui.addestate

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.chip.Chip
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.AddEstateFragmentBinding
import com.openclassrooms.realestatemanager.photos.*
import com.openclassrooms.realestatemanager.ui.EstatesViewModel
import com.openclassrooms.realestatemanager.ui.MainActivity
import com.openclassrooms.realestatemanager.ui.adjustments.AdjustmentsPhotoFragment
import com.openclassrooms.realestatemanager.ui.listview.ListViewFragment
import com.openclassrooms.realestatemanager.utils.TAG_ADD_ESTATE_FRAGMENT
import com.openclassrooms.realestatemanager.utils.TAG_LIST_VIEW_FRAGMENT
import com.openclassrooms.realestatemanager.utils.stringAddressToLocation
import kotlinx.android.synthetic.main.add_estate_fragment.*
import java.io.File
import java.util.*


class AddEstateFragment : Fragment() {

    // VIEW MODELS & DATA BINDING
    private lateinit var viewDataBinding: AddEstateFragmentBinding
    private val viewModel: AddEstateViewModel by lazy {
        ViewModelProviders.of(this).get(AddEstateViewModel::class.java)
    }
    private val estatesViewModel: EstatesViewModel by lazy {
        ViewModelProviders.of(activity!!).get(EstatesViewModel::class.java)
    }


    // STATUS ITEM PER DEFAULT
    private var itemStatus: Int = 0
    // NEAR PLACES
    private val placesChoices by lazy { context!!.resources.getStringArray(R.array.add_estate_near_choices) }
    // PHOTOS
    private var holdersPhoto = mutableListOf<ConstraintLayout?>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.add_estate_fragment, container, false)
        (activity as MainActivity).hideBottomNavigation(true)

        // DATA BINDING
        viewDataBinding = AddEstateFragmentBinding.bind(rootView).apply {
            this.viewmodel = viewModel
        }
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
            setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp)
            // title
            title =
                context!!.resources.getString(R.string.add_estate_title)
        }
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHasOptionsMenu(true)

        // Init view model
        viewModel.init(
            context!!.resources.getString(R.string.add_estate_status_available),
            placesChoices.size, getCurrency()!!
        )

        // Listeners
        setOnClickListeners()

        // Observe new photo added
        viewModel.atLeastOnePhoto.observe(
            viewLifecycleOwner,
            Observer { t -> if (t) updateViewWithPhotos(viewModel.photoPathList.value!!) })
        // Listeners
        setOnClickListeners()

        // Observe when a new estate is created in viewModel
        setObserverAddEstate()
    }

    private fun getCurrency(): String? {
        val prefs: SharedPreferences =
            context!!.getSharedPreferences("preferences", Context.MODE_PRIVATE)
        return prefs.getString("pref_currency", null)
    }


    private fun setOnClickListeners() {
        // --- DATE PICKERS --- //
        // date available
        viewDataBinding.addEstateDateAvailable.editText?.setOnClickListener {
            displayDatePickerPopUp(
                false
            )
        }
        // date sold
        viewDataBinding.addEstateDateSold.editText?.setOnClickListener { displayDatePickerPopUp(true) }

        // -- NEARS PLACES -- //
        viewDataBinding.addEstateNear.setOnClickListener { showNearChoicesDialog() }
        viewModel.nearPlaces.observe(viewLifecycleOwner, Observer { t -> (addChipsToView(t!!)) })

        // -- STATUS -- //
        viewDataBinding.addEstateStatus.editText?.setOnClickListener { showStatusChoicesDialog() }

        // -- PHONE BUTTONS -- //
        // camera
        viewDataBinding.addEstateLoadFromCameraBtn.setOnClickListener { displayCameraIntent() }
        // gallery
        viewDataBinding.addEstateLoadFromGalleryBtn.setOnClickListener { displayGalleryIntent() }
    }

    // ADD NEW ESTATE LISTENER
    private fun setObserverAddEstate() {
        viewModel.addNewEstate.observe(
            this,
            Observer { t ->
                if (t) {
                    // To convert the address to a List<Double> with latitude and longitude
                    val addressFormatted = viewModel.newEstate.address
                    viewModel.newEstate.latLng =
                        stringAddressToLocation(context!!, addressFormatted.replace("-", ""))
                  estatesViewModel.createEstate(viewModel.newEstate)
                    (activity as MainActivity).setFragment(
                        ListViewFragment.newInstance(),
                        false,
                        TAG_LIST_VIEW_FRAGMENT
                    )
                }
            })
    }


    /********** DATE PICKERS **********/
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


    /********** STATUS **********/
    private fun showStatusChoicesDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(context!!)
        val choices = context!!.resources.getStringArray(R.array.add_estate_status_choices)
        builder.apply {
            setCancelable(true)
            setSingleChoiceItems(
                choices, itemStatus
            ) { dialog, which ->
                dialog.dismiss()
                viewModel.status.value = choices[which]
                viewModel.hasBeenSold.value = (which == 1)
                itemStatus = which
            }
            create()
            show()
        }
    }


    /********** NEAR PLACES **********/
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
        add_estate_near_chips_box.removeAllViews()
        for (place in list) {
            val chip =
                layoutInflater.inflate(
                    R.layout.chip_layout,
                    add_estate_near_chips_box,
                    false
                ) as Chip
            chip.text = place
            chip.setOnClickListener { showNearChoicesDialog() }
            add_estate_near_chips_box.addView(chip)
        }
    }


    /********** PHOTO **********/
    private fun displayCameraIntent() {
        val newPhoto: File = createFile(context!!)
        viewModel.newPhotoPath = newPhoto.absolutePath
        val camera: Intent? = getCameraIntent(context!!, newPhoto)
        startActivityForResult(camera, REQUEST_IMAGE_CAPTURE)
    }

    private fun displayGalleryIntent() {
        val newPhoto: File = createFile(context!!)
        viewModel.newPhotoPath = newPhoto.absolutePath
        startActivityForResult(getGalleryIntent(), REQUEST_GALLERY)
    }

    private fun addNewPhotoToModel(path: String) {
        // The path to the file of the photo
        viewModel.photoPathList.value?.add(path)
        // The title of the photo : "(no title)" per default
        viewModel.photoTitleList.value?.add(context!!.resources.getString(R.string.add_estate_no_title))
        viewModel.atLeastOnePhoto.value = true
    }

    private fun updateViewWithPhotos(listPaths: List<String?>) {
        holdersPhoto = mutableListOf()
        viewDataBinding.addEstatePhotosLayout.removeAllViews()
        val listTitles = viewModel.photoTitleList.value
        for ((index, path) in listPaths.withIndex()) {
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
            viewDataBinding.addEstatePhotosLayout.addView(photoHolder)
        }
    }

    private fun onDeletePhotoBtnClicked(index: Int) {
        holdersPhoto[index]?.visibility = View.GONE
        viewModel.photoPathList.value?.removeAt(index)
        viewModel.photoTitleList.value?.removeAt(index)
        viewModel.atLeastOnePhoto.value = !viewModel.photoTitleList.value.isNullOrEmpty()
    }

    private fun onTitlePhotoChanged(index: Int): TextWatcher {
        return object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.toString().isEmpty()) viewModel.photoTitleList.value!![index] =
                    context!!.resources.getString(R.string.add_estate_no_title)
                else viewModel.photoTitleList.value!![index] = s.toString()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }
    }

    // Callback from AdjustmentsPhotoFragment
    fun updateAdjustedPhoto() {
        val photoPath = viewModel.newPhotoPath
        addNewPhotoToModel(photoPath!!)
    }


    // ON ACTIVITY RESULT (CAMERA OR GALLERY INTENT)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {

                // RESULT FROM CAMERA INTENT
                REQUEST_IMAGE_CAPTURE -> {
                    // Display fragment to rotate image
                    view?.clearFocus()
                    (activity as MainActivity).setFragmentOnTopOfView(
                        AdjustmentsPhotoFragment.newInstance(
                            viewModel.newPhotoPath!!,
                            TAG_ADD_ESTATE_FRAGMENT
                        ), true
                    )
                }

                // RESULT FROM GALLERY INTENT
                REQUEST_GALLERY -> {
                    lateinit var originalBitmap: Bitmap
                    if (android.os.Build.VERSION.SDK_INT >= 29) {
                        // To handle depreciation use
                        originalBitmap = ImageDecoder.decodeBitmap(
                            ImageDecoder.createSource(
                                context!!.contentResolver,
                                data!!.data!!
                            )
                        )
                    } else {
                        // Use older version
                        @Suppress("DEPRECATION")
                        originalBitmap =
                            MediaStore.Images.Media.getBitmap(
                                context!!.contentResolver,
                                data!!.data
                            )
                    }
                    // resize bitmap if too big
                    val bitmap = getResizedBitmap(originalBitmap)
                    replaceFileWithChangedBitmap(viewModel.newPhotoPath!!, bitmap)
                    addNewPhotoToModel(viewModel.newPhotoPath!!)
                }
            }
        }
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
        const val REQUEST_IMAGE_CAPTURE = 12
        const val REQUEST_GALLERY = 13

        fun newInstance() = AddEstateFragment()
    }
}

