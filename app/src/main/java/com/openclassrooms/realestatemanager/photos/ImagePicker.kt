package com.openclassrooms.realestatemanager.photos

import android.content.Context
import android.content.Intent
import android.provider.MediaStore
import androidx.core.content.FileProvider
import java.io.File


class ImagePicker {

    companion object {

        fun getCameraIntent(
            context: Context,
            getImage: File?
        ): Intent? {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

            val uri = FileProvider.getUriForFile(
                context, "com.openclassrooms.realestatemanager.fileprovider",
                getImage!!
            )
            return intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        }


        fun getGalleryIntent(): Intent? {
            val galleryIntent = Intent(Intent.ACTION_GET_CONTENT)
            galleryIntent.type = "image/*"
            return galleryIntent
        }
    }
}