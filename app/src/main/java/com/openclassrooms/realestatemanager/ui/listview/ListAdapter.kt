package com.openclassrooms.realestatemanager.ui.listview

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.model.Estate
import com.openclassrooms.realestatemanager.utils.formatPrice
import kotlinx.android.synthetic.main.item_list.view.*

class ListAdapter(
    contextFragment: Context,
    private val estates: List<Estate>,
    val listener: (id: Long?) -> Unit
) :
    RecyclerView.Adapter<ListAdapter.ListViewHolder>() {

    val context: Context = contextFragment

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list, parent, false)
        return ListViewHolder(view)
    }

    override fun getItemCount() = estates.size


    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.type?.text = estates[position].type
        holder.address?.text = estates[position].address
        holder.price?.text = formatPrice(estates[position].priceDollars)
        holder.photo?.setImageURI(Uri.parse(estates[position].pathPhotos[0]))

        holder.itemView.setOnClickListener { listener(estates[position].id) }
    }


    class ListViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        val type: TextView? = v.item_list_type_tv
        val price: TextView? = v.item_list_price_tv
        val address: TextView? = v.item_list_place_tv
        val photo: ImageView? = v.item_list_img


    }
}

