package com.openclassrooms.realestatemanager

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.openclassrooms.realestatemanager.utils.Utils
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NetworkTest {

    @Test
    fun checkIfNetworkIsAvailable() {
        assertTrue(
            Utils.isNetworkAvailable(InstrumentationRegistry.getInstrumentation().context)!!
        )
    }
}