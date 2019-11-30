package com.openclassrooms.realestatemanager.ui.addestate

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.photos.replaceFileWithChangedBitmap
import com.openclassrooms.realestatemanager.photos.rotateImage
import com.openclassrooms.realestatemanager.ui.MainActivity
import com.openclassrooms.realestatemanager.utils.TAG_ADD_ESTATE_FRAGMENT
import kotlinx.android.synthetic.main.adjustments_photo_fragment.*

class AdjustmentsPhotoFragment : Fragment() {

    var bitmap: Bitmap? = null
    lateinit var path: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as MainActivity).supportActionBar?.hide()
        return inflater.inflate(R.layout.adjustments_photo_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setImageView()
        setOnRotateButtonClickedListener()
        setOnConfirmButtonClickedListener()
    }

    private fun setImageView() {
        path = arguments!!.getString(KEY_PHOTO_PATH)!!
        bitmap = BitmapFactory.decodeFile(path)
        adjustments_photo.setImageBitmap(bitmap)
    }

    private fun setOnRotateButtonClickedListener() {
        adjustments_rotate_btn.setOnClickListener {
            bitmap = rotateImage(bitmap!!, 90)
            adjustments_photo.setImageBitmap(bitmap)
        }
    }

    private fun setOnConfirmButtonClickedListener() {
        adjustments_confirm_btn.setOnClickListener {
            replaceFileWithChangedBitmap(path, bitmap!!)
            (activity as MainActivity).supportActionBar?.show()
            (activity as MainActivity).removeFragment(this)
            (fragmentManager?.findFragmentByTag(TAG_ADD_ESTATE_FRAGMENT) as AddEstateFragment).updateAdjustedPhoto()
        }
    }

    companion object {

        fun newInstance(path: String): Fragment {
            val fragment = AdjustmentsPhotoFragment()
            val args = Bundle()
            args.putString(KEY_PHOTO_PATH, path)
            fragment.arguments = args
            return fragment
        }

        private const val KEY_PHOTO_PATH: String = "KEY_PHOTO_PATH"
    }


}