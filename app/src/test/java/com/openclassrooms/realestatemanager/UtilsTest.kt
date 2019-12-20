package com.openclassrooms.realestatemanager


import com.openclassrooms.realestatemanager.utils.Utils.convertDollarToEuro
import com.openclassrooms.realestatemanager.utils.Utils.convertEuroToDollar
import com.openclassrooms.realestatemanager.utils.Utils.getTodayDate
import org.junit.Assert
import org.junit.Test
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class UtilsTest {

    @Test
    fun convertDollarToEuroTest() {
        val dollars = 100
        val euros: Int = convertDollarToEuro(dollars)
        Assert.assertEquals(81, euros)
    }

    @Test
    fun convertEuroToDollarTest() {
        val euros = 81
        val dollars: Int = convertEuroToDollar(euros)
        Assert.assertEquals(100, dollars)
    }

    @Test
    fun getTodayTest() {
        val today = Date()
        val dateFormat: DateFormat = SimpleDateFormat("dd/MM/yyyy")
        Assert.assertEquals(dateFormat.format(today), getTodayDate())
    }

}