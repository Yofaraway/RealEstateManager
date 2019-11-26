package com.openclassrooms.realestatemanager.utils

import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*


fun stringToDate(str: String): Date? {
    val formatter = SimpleDateFormat("dd/mm/yyyy", Locale.getDefault())
    return formatter.parse(str)
}

fun dateToString(cal: Calendar): String? {
    val date:Date = cal.time
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