package com.openclassrooms.realestatemanager.utils

import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

fun stringToDate(str: String):Date {
    val formatter = SimpleDateFormat("dd/mm/yyyy", Locale.getDefault())
    return formatter.parse(str)
}

fun formatPrice(price: Int): String? {
    val formatter = DecimalFormat("#,###")
    return "$" + formatter.format(price)
}