package com.openclassrooms.realestatemanager.utils

import java.text.SimpleDateFormat
import java.util.*

fun stringToDate(str: String):Date {
    val formatter = SimpleDateFormat("dd/mm/yyyy", Locale.getDefault())
    return formatter.parse(str)
}