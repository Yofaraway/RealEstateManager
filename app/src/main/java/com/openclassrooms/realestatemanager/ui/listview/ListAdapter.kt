package com.openclassrooms.realestatemanager.ui.listview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.model.Estate
import kotlinx.android.synthetic.main.item_list.view.*

class ListAdapter(private val estates: List<Estate>) :
    RecyclerView.Adapter<ListAdapter.ListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list, parent, false)
        return ListViewHolder(view)
    }

    override fun getItemCount() = estates.size


    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.type.text = estates[position].type
    }

    class ListViewHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {

        val type = v.item_list_type_tv

        init {
            v.setOnClickListener(this)
        }


        override fun onClick(v: View?) {
            println("oui")
        }
    }
}

