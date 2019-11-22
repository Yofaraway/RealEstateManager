package com.openclassrooms.realestatemanager.photos

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Environment
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.openclassrooms.realestatemanager.R
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


@Throws(IOException::class)
fun createFile(context: Context): File {
    // Create an image file name
    val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)

    return File.createTempFile(
        "JPEG_${timeStamp}_", /* prefix */
        ".jpg", /* suffix */
        storageDir /* directory */
    )
}

fun getLayout(context: Context, index: Int): ConstraintLayout? {
    val layout = ConstraintLayout(context)
    val lp = ConstraintLayout.LayoutParams(
        ViewGroup.LayoutParams.WRAP_CONTENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    )

    return layout.apply {
        layoutParams = lp
        tag = index
    }
}

fun getImageViewFromBitmap(context: Context, selectedImage: Bitmap): ImageView? {
    val image = ImageView(context)
    val lp = ConstraintLayout.LayoutParams(300, 400)
    lp.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
    lp.startToStart = ConstraintLayout.LayoutParams.PARENT_ID
    return image.apply {
        setImageBitmap(selectedImage)
        layoutParams = lp
        scaleType = ImageView.ScaleType.CENTER_CROP
        setPadding(5, 1, 5, 1)
    }
}

fun getImageViewFromContentURI(context: Context, uri: Uri): ImageView? {
    val image = ImageView(context)
    val lp = ConstraintLayout.LayoutParams(300, 400)
    lp.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
    lp.startToStart = ConstraintLayout.LayoutParams.PARENT_ID
    return image.apply {
        setImageURI(uri)
        layoutParams = lp
        scaleType = ImageView.ScaleType.CENTER_CROP
        setPadding(5, 1, 5, 1)
    }
}


fun getEditText(context: Context, index: Int): EditText? {
    val titlePhoto = EditText(context)
    val lp =
        ConstraintLayout.LayoutParams(300, ViewGroup.LayoutParams.WRAP_CONTENT)
    lp.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
    lp.startToStart = ConstraintLayout.LayoutParams.PARENT_ID
    val maxLength = 16
    val filterArray = arrayOfNulls<InputFilter>(1)
    filterArray[0] = LengthFilter(maxLength)

    return titlePhoto.apply {
        layoutParams = lp
        hint = context.resources.getString(R.string.add_estate_no_title)
        textAlignment = View.TEXT_ALIGNMENT_CENTER
        maxLines = 1
        filters = filterArray
        setBackgroundColor(Color.parseColor("#80FFFFFF")) // white 50% transparent
        tag = index
    }
}

fun getDeleteButton(context: Context, index: Int): ImageView? {
    val delete = ImageView(context)

    val lp = ConstraintLayout.LayoutParams(
        ViewGroup.LayoutParams.WRAP_CONTENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    )
    lp.apply {
        topToTop = ConstraintLayout.LayoutParams.PARENT_ID
        endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
        setMargins(2, 2, 2, 2)
    }

    return delete.apply {
        setImageResource(R.drawable.remove_photo_icon)
        layoutParams = lp
        tag = index
    }
}



