package yofaraway.openclassrooms.realestatemanager.ui.listview

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_list.view.*
import yofaraway.openclassrooms.realestatemanager.R
import yofaraway.openclassrooms.realestatemanager.model.Estate
import yofaraway.openclassrooms.realestatemanager.utils.Utils.convertDollarToEuro
import yofaraway.openclassrooms.realestatemanager.utils.Utils.formatPrice
import yofaraway.openclassrooms.realestatemanager.utils.Utils.getAddress
import yofaraway.openclassrooms.realestatemanager.utils.Utils.getCity

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
        holder.address?.text = getFormattedAddress(estates[position].address)
        holder.price?.text = getPriceWithCurrentCurrency(estates[position].priceDollars)
        holder.photo?.setImageURI(Uri.parse(estates[position].photosPathList[0]))

        holder.itemView.setOnClickListener { listener(estates[position].id) }
    }

    private fun getPriceWithCurrentCurrency(priceDollars: Int): String? {
        return when (val currency = getCurrency()) {
            "Euro" -> formatPrice(convertDollarToEuro(priceDollars), currency)
            else -> formatPrice(priceDollars, currency!!)
        }
    }

    private fun getCurrency(): String? {
        val prefs: SharedPreferences =
            context.getSharedPreferences("preferences", Context.MODE_PRIVATE)
        return prefs.getString("pref_currency", null)
    }

    private fun getFormattedAddress(str: String): String {
        val address = getAddress(str)
        val addressWord = context.resources.getString(R.string.list_address_word)
        val city = getCity(str)
        return "$address $addressWord $city"
    }


    class ListViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        val type: TextView? = v.item_list_type_tv
        val price: TextView? = v.item_list_price_tv
        val address: TextView? = v.item_list_place_tv
        val photo: ImageView? = v.item_list_img


    }
}

