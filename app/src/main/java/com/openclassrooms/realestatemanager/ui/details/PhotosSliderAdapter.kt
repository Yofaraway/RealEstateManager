package com.openclassrooms.realestatemanager.ui.details

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.openclassrooms.realestatemanager.R
import kotlinx.android.synthetic.main.photo_slider_items.view.*


class PhotosSliderAdapter(contextParent: Context, val listPhotos: List<String>, val listTitlesPhotos: List<String>) :
    PagerAdapter() {

    val context = contextParent

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        // layout
        val inflater = LayoutInflater.from(context)
        val layout =
            inflater.inflate(R.layout.photo_slider_items, container, false) as ViewGroup
        // photo
        val uri = Uri.parse(listPhotos[position])
        val photoView: ImageView = layout.pagerImage
        photoView.setImageURI(uri)
        // title
        val title = listTitlesPhotos[position]
        val titleView: TextView = layout.pagerTitle
       titleView.text = title

        container.addView(layout)
        return layout

    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun getCount(): Int {
        return this.listPhotos.size
    }


}