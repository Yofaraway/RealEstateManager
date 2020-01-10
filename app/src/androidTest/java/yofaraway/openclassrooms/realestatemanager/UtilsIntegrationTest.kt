package yofaraway.openclassrooms.realestatemanager

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import yofaraway.openclassrooms.realestatemanager.utils.Utils

@RunWith(AndroidJUnit4::class)
class UtilsIntegrationTest {

    @Test
    fun checkIfNetworkIsAvailable() {
        assertTrue(
            Utils.isNetworkAvailable(InstrumentationRegistry.getInstrumentation().context)!!
        )
    }

    @Test
    fun stringAddressToLocationTest() {
        val address = getFullAddress()
        val latLng = Utils.stringAddressToLocation(
            InstrumentationRegistry.getInstrumentation().context,
            address
        )
        Assert.assertEquals("48.8701231", latLng[0])
        Assert.assertEquals("2.3165784", latLng[1])

    }

    companion object {
        // A full address in the db looks like : "Address|City|ZipCode"
        private fun getFullAddress(): String {
            return "55, Rue du Faubourg Saint-Honoré|Paris|75008"
        }
    }

}