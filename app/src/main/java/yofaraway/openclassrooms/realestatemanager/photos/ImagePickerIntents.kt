package yofaraway.openclassrooms.realestatemanager.photos

import android.content.Context
import android.content.Intent
import android.provider.MediaStore
import androidx.core.content.FileProvider
import java.io.File


fun getCameraIntent(
    context: Context,
    getImage: File?
): Intent? {
    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

    val uri = FileProvider.getUriForFile(
        context, "yofaraway.openclassrooms.realestatemanager.fileprovider",
        getImage!!
    )
    return intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
}


fun getGalleryIntent(): Intent? {
    val galleryIntent = Intent(Intent.ACTION_OPEN_DOCUMENT)
    galleryIntent.type = "image/*"
    return galleryIntent
}

