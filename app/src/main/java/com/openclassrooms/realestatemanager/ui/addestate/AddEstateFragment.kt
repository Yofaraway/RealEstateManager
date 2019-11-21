package com.openclassrooms.realestatemanager.ui.addestate

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.AddEstateFragmentBinding
import com.openclassrooms.realestatemanager.photos.ImageHelper
import com.openclassrooms.realestatemanager.photos.getCameraIntent
import com.openclassrooms.realestatemanager.photos.getGalleryIntent
import com.openclassrooms.realestatemanager.ui.EstatesViewModel
import com.openclassrooms.realestatemanager.ui.MainActivity
import com.openclassrooms.realestatemanager.ui.listview.ListViewFragment
import kotlinx.android.synthetic.main.add_estate_fragment.*
import java.io.File
import java.util.*


class AddEstateFragment : Fragment() {

    // VIEWMODEL & DATA BINDING
    private lateinit var viewDataBinding: AddEstateFragmentBinding
    private val viewModel: AddEstateViewModel by lazy {
        ViewModelProviders.of(this).get(AddEstateViewModel::class.java)
    }
    private val estatesViewModel: EstatesViewModel by lazy {
        ViewModelProviders.of(activity!!).get(EstatesViewModel::class.java)
    }
    // LOAD PHOTOS
    private lateinit var currentPhotoPath: String
    private var holdersList: MutableList<ConstraintLayout?> = mutableListOf()
    private val cameraBtn: ImageButton by lazy { add_estate_load_from_camera_btn }
    private val galleryBtn: ImageButton by lazy { add_estate_load_from_gallery_btn }
    private val photosLayout: LinearLayout by lazy { add_estate_photos_layout }
    private var index = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(
            R.layout.add_estate_fragment,
            container,
            false
        )
        // DATA BINDING
        viewDataBinding = AddEstateFragmentBinding.bind(rootView).apply {
            this.viewmodel = viewModel
        }
        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner
        return viewDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setTitle()
        setOnCameraBtnClick()
        setOnGalleryBtnClick()
        viewModel.init()
        observeDatePickers()
        observeNewEstate()
    }

    private fun observeDatePickers() {
        // When one of the date pickers is clicked, its boolean in the viewmodel
        // is set to true (by data binding) so we can observe it from there and
        // open the DatePickerDialog
        viewModel.dateAvailableDatePicker.observe(
            this,
            Observer { t -> if (t) displayDatePickerPopUp(false) })
        viewModel.dateSoldDatePicker.observe(
            this,
            Observer { t -> if (t) displayDatePickerPopUp(true) })
    }

    private fun observeNewEstate() {
        viewModel.addNewEstate.observe(
            this,
            Observer { t ->
                if (t) {
                    estatesViewModel.createEstate(viewModel.newEstate)
                    (activity as MainActivity).setFragment(ListViewFragment.newInstance())
                }
            })
    }

    private fun setTitle() {
        (activity as AppCompatActivity).supportActionBar?.title =
            context!!.resources.getString(R.string.add_estate_title)
    }

    // BUTTONS

    private fun setOnCameraBtnClick() {
        cameraBtn.setOnClickListener {
            val newPhoto: File = ImageHelper.createFile(context!!)
            currentPhotoPath = newPhoto.absolutePath
            val camera: Intent? = getCameraIntent(context!!, newPhoto)
            startActivityForResult(camera, REQUEST_IMAGE_CAPTURE)
        }
    }

    private fun setOnGalleryBtnClick() {
        galleryBtn.setOnClickListener {
            startActivityForResult(getGalleryIntent(), REQUEST_GALLERY)
        }
    }

    // ON ACTIVITY RESULT

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                // RESULT FROM CAMERA INTENT
                REQUEST_IMAGE_CAPTURE -> {
                    addNewPhotoToViewModel(currentPhotoPath)
                    val bitmap: Bitmap = BitmapFactory.decodeFile(currentPhotoPath)
                    addNewPhotoToView(ImageHelper.getImageViewFromBitmap(context!!, bitmap))
                    index++
                }

                // RESULT FROM GALLERY INTENT
                REQUEST_GALLERY -> {
                    addNewPhotoToViewModel(data!!.dataString!!)
                    addNewPhotoToView(
                        ImageHelper.getImageViewFromContentURI(
                            context!!,
                            data.data!!
                        )
                    )
                    index++
                }
            }
        }
    }

    private fun addNewPhotoToViewModel(path: String) {
        // The path to the file of the photo
        viewModel.pathToPhotos.value?.add(path)
        // The title of the photo : "(no title)" per default
        viewModel.titlesPhotos.value?.add(context!!.resources.getString(R.string.add_estate_no_title))
        if (viewModel.atLeastOnePhoto.value == false) viewModel.atLeastOnePhoto.value = true
    }

    private fun addNewPhotoToView(imageView: ImageView?) {
        // Holder of ImageView + EditText + Button
        val photoHolder: ConstraintLayout? = ImageHelper.getLayout(context!!, index)
        photoHolder!!.addView(imageView)
        // EditText
        val editText: EditText? = ImageHelper.getEditText(context!!, index)
        //editText!!.addTextChangedListener{}
        photoHolder.addView(editText)
        // Delete button
        val deletePhotoBtn: ImageView? = ImageHelper.getDeleteButton(context!!, index)
        deletePhotoBtn!!.setOnClickListener { deletePhoto(deletePhotoBtn.tag.toString().toInt()) }
        photoHolder.addView(deletePhotoBtn)
        holdersList.add(photoHolder)
        // Layout of all photos
        photosLayout.addView(photoHolder)
    }


    private fun deletePhoto(tag: Int) {
        // remove from view
        holdersList[tag]?.visibility = View.GONE
        holdersList.removeAt(tag)
        holdersList.add(null)
        // remove from viewmodel (we replace the removed value with null so our index still works)
        viewModel.pathToPhotos.value?.removeAt(tag)
        viewModel.pathToPhotos.value?.add(null)
        viewModel.titlesPhotos.value?.removeAt(tag)
        viewModel.titlesPhotos.value?.add(null)

        // check if all values are null
        if (!viewModel.pathToPhotos.value!!.any { it != null })
            viewModel.atLeastOnePhoto.value = false
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
                if (!dateSold) {
                    viewModel.dateAvailable.value = datePicked
                    viewModel.dateAvailableDatePicker.value = false
                } else {
                    viewModel.dateSold.value = datePicked
                    viewModel.dateSoldDatePicker.value = false
                }
            }, y, m, d
        ).show()
    }

    companion object {
        const val REQUEST_IMAGE_CAPTURE = 12
        const val REQUEST_GALLERY = 13

        fun newInstance() = AddEstateFragment()
    }


}