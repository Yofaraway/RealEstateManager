package com.openclassrooms.realestatemanager.utils

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.util.Log
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*


fun stringToDate(str: String): Date? {
    val formatter = SimpleDateFormat("dd/mm/yyyy", Locale.getDefault())
    return formatter.parse(str)
}

fun dateToString(cal: Calendar): String? {
    val date: Date = cal.time
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return formatter.format(date)
}

fun formatPrice(int: Int): String? {
    val formatter = DecimalFormat("#,###")
    return "$" + formatter.format(int)
}

fun formatSurface(int: Int): String? {
    val formatter = DecimalFormat("#,###")
    return formatter.format(int) + " m²"
}

fun stringAddressToLocation(context: Context, address: String): List<String?> {
    var list: List<String?> = mutableListOf()

    val geo =
        Geocoder(context, Locale.getDefault())
    val addresses: List<Address> =
        geo.getFromLocationName(address, 1)
    if (addresses.isEmpty()) {
        Log.d("AddEstate", "No address location found = no marker will be add to the map")
    } else {
        list = mutableListOf(addresses[0].latitude.toString(), addresses[0].longitude.toString())
    }
    return list
}


fun getAddress(str: String): String {
    return str.substring(0, str.indexOf("-"))
}

fun getCity(str: String): String {
    return str.substring(str.indexOf("-") + 1, str.lastIndexOf("-"))
}

fun getZipCode(str: String): String {
    return str.substring(str.lastIndexOf("-") + 1)
}