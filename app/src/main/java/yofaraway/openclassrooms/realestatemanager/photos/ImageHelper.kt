package yofaraway.openclassrooms.realestatemanager.photos

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Matrix
import android.net.Uri
import android.os.Environment
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.text.InputType
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.setPadding
import yofaraway.openclassrooms.realestatemanager.R
import java.io.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.min


@Throws(IOException::class)
fun createFile(context: Context): File {
    // Create an image file name
    val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
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
    lp.setMargins(10, 0, 10, 0)

    return layout.apply {
        layoutParams = lp
        tag = index
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


fun getEditText(context: Context, index: Int, text: String?): EditText? {
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
        if (text.isNullOrBlank() || text == context.resources.getString(R.string.add_estate_no_title))
            this.hint = context.resources.getString(R.string.add_estate_no_title)
        else this.setText(text)
        textAlignment = View.TEXT_ALIGNMENT_CENTER
        inputType = InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
        maxLines = 1
        filters = filterArray
        setBackgroundColor(Color.parseColor("#80FFFFFF")) // white 50% transparent
        tag = index
        setPadding(0)
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
    }

    return delete.apply {
        setImageResource(R.drawable.remove_photo_icon)
        layoutParams = lp
        tag = index
        setPadding(2, 2, 2, 2)
    }
}


fun getResizedBitmap(bm: Bitmap): Bitmap {
    val maxHeight = 1024
    val maxWidth = 1024
    val scale: Float = min(
        maxHeight.toFloat() / bm.width,
        maxWidth.toFloat() / bm.height
    )
    val matrix = Matrix()
    matrix.postScale(scale, scale)

    // "RECREATE" THE NEW BITMAP
    return Bitmap.createBitmap(
        bm, 0, 0, bm.width, bm.height, matrix, false
    )
}


fun rotateImage(img: Bitmap, degree: Int): Bitmap {
    val matrix = Matrix()
    matrix.postRotate(degree.toFloat())
    val rotatedImg =
        Bitmap.createBitmap(img, 0, 0, img.width, img.height, matrix, true)
    img.recycle()
    return rotatedImg
}

fun replaceFileWithChangedBitmap(path: String, bitmap: Bitmap) {
    val file = File(path)
    val os: OutputStream = BufferedOutputStream(FileOutputStream(file))
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os)
    os.close()
}



