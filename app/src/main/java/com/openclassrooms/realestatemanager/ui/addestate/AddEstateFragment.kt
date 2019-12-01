package com.openclassrooms.realestatemanager.ui.addestate

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.EditText
import android.widget.ImageView
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
import com.openclassrooms.realestatemanager.ui.listview.ListViewFragment
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


    // PHOTOS
    private var holdersList: MutableList<ConstraintLayout?> = mutableListOf()
    // STATUS ITEM PER DEFAULT
    private var itemStatus: Int = 0
    // NEAR PLACES
    private val placesChoices by lazy { context!!.resources.getStringArray(R.array.add_estate_near_choices) }


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
            placesChoices.size
        )

        // Listeners
        setOnClickListeners()

        // Observe when a new estate is created in viewModel
        setObserverNewEstate()
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


    private fun setObserverNewEstate() {
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


    /** PHOTO BUTTONS **/
    private fun displayCameraIntent() {
            val newPhoto: File = createFile(context!!)
            viewModel.newPhotoPath = newPhoto.absolutePath
            val camera: Intent? = getCameraIntent(context!!, newPhoto)
            startActivityForResult(camera, REQUEST_IMAGE_CAPTURE)
    }

    private fun displayGalleryIntent() {
        startActivityForResult(getGalleryIntent(), REQUEST_GALLERY)
    }


    private fun addNewPhotoToModel(path: String) {
        // The path to the file of the photo
        viewModel.pathToPhotos.value?.add(path)
        // The title of the photo : "(no title)" per default
        viewModel.titlesPhotos.value?.add(context!!.resources.getString(R.string.add_estate_no_title))
        if (viewModel.atLeastOnePhoto.value == false) viewModel.atLeastOnePhoto.value = true
    }

    private fun addNewPhotoToView(imageView: ImageView?) {
        // Holder of ImageView + EditText + Button
        val photoHolder: ConstraintLayout? = getLayout(context!!, viewModel.indexPhotos)
        photoHolder!!.addView(imageView)
        // EditText
        val editText: EditText? = getEditText(context!!, viewModel.indexPhotos, null)
        editText!!.addTextChangedListener(onTitlePhotoChanged(editText.tag.toString().toInt()))
        photoHolder.addView(editText)
        // Delete button
        val deletePhotoBtn: ImageView? = getDeleteButton(context!!, viewModel.indexPhotos)
        deletePhotoBtn!!.setOnClickListener { onDeletePhotoBtnClicked(deletePhotoBtn.tag.toString().toInt()) }
        photoHolder.addView(deletePhotoBtn)
        holdersList.add(photoHolder)
        // Layout of all photos
        viewDataBinding.addEstatePhotosLayout.addView(photoHolder)
    }


    private fun onDeletePhotoBtnClicked(tag: Int) {
        // remove from view
        holdersList[tag]!!.visibility = View.GONE
        holdersList[tag] = null
        // remove from viewmodel (we replace the removed value with null so our index still works)
        viewModel.pathToPhotos.value!![tag] = null
        viewModel.titlesPhotos.value!![tag] = null
        // check if all values are null
        if (!viewModel.pathToPhotos.value!!.any { it != null })
            viewModel.atLeastOnePhoto.value = false
    }

    private fun onTitlePhotoChanged(tag: Int): TextWatcher {
        return object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.toString() == "") viewModel.titlesPhotos.value!![tag] =
                    context!!.resources.getString(R.string.add_estate_no_title)
                else viewModel.titlesPhotos.value!![tag] = s.toString()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }
    }

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


    // Callback from AdjustmentsPhotoFragment
    fun updateAdjustedPhoto() {
        val photoPath = viewModel.newPhotoPath
        addNewPhotoToModel(photoPath!!)
        val bitmap = BitmapFactory.decodeFile(photoPath)
        addNewPhotoToView(getImageViewFromBitmap(context!!, bitmap))
        viewModel.indexPhotos++
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
                        AdjustmentsPhotoFragment.newInstance(viewModel.newPhotoPath!!), true
                    )
                }

                // RESULT FROM GALLERY INTENT
                REQUEST_GALLERY -> {
                    // Give permanent permission to read the uri (needed after a reboot of the device)
                    val takeFlags = data?.flags?.and(Intent.FLAG_GRANT_READ_URI_PERMISSION)

                    try {
                        activity!!.contentResolver.takePersistableUriPermission(
                            data!!.data!!,
                            takeFlags!!
                        )
                    } catch (e: SecurityException) {
                        e.printStackTrace()
                    }

                    addNewPhotoToModel(data?.dataString!!)
                    addNewPhotoToView(
                        getImageViewFromContentURI(
                            context!!,
                            data.data!!
                        )
                    )
                    viewModel.indexPhotos++
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

