package yofaraway.openclassrooms.realestatemanager


import org.junit.Assert
import org.junit.Test
import yofaraway.openclassrooms.realestatemanager.utils.Utils.convertDollarToEuro
import yofaraway.openclassrooms.realestatemanager.utils.Utils.convertEuroToDollar
import yofaraway.openclassrooms.realestatemanager.utils.Utils.formatPrice
import yofaraway.openclassrooms.realestatemanager.utils.Utils.formatSurface
import yofaraway.openclassrooms.realestatemanager.utils.Utils.getAddress
import yofaraway.openclassrooms.realestatemanager.utils.Utils.getCity
import yofaraway.openclassrooms.realestatemanager.utils.Utils.getTodayDate
import yofaraway.openclassrooms.realestatemanager.utils.Utils.getZipCode
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


    @Test
    fun getAddressTest() {
        val address = getAddress(getFullAddress())
        Assert.assertEquals("55, Rue du Faubourg Saint-Honoré", address)
    }

    @Test
    fun getCityTest() {
        val city = getCity(getFullAddress())
        Assert.assertEquals("Paris", city)
    }

    @Test
    fun getZipCodeTest() {
        val zipCode = getZipCode(getFullAddress())
        Assert.assertEquals("75008", zipCode)
    }


    @Test
    fun formatPriceDollarTest(){
        val price = 100000
        val priceDollar = formatPrice(price, "Dollar")
        Assert.assertEquals("$100,000", priceDollar)
    }

    @Test
    fun formatPriceEuroTest(){
        val price = 100000
        val priceDollar = formatPrice(price, "Euro")
        Assert.assertEquals("100,000€", priceDollar)
    }

    @Test
    fun formatSurfaceTest(){
        val surface = 1000
        val surfaceFormatted = formatSurface(surface)
        Assert.assertEquals("1,000 m²", surfaceFormatted)
    }


    companion object {
        // A full address in the db looks like : "Address|City|ZipCode"
        private fun getFullAddress(): String {
            return "55, Rue du Faubourg Saint-Honoré|Paris|75008"
        }
    }
}