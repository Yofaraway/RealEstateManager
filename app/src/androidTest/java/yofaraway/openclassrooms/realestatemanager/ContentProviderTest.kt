package yofaraway.openclassrooms.realestatemanager

import android.content.ContentResolver
import android.content.ContentUris
import android.content.ContentValues
import android.database.Cursor
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.notNullValue
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import yofaraway.openclassrooms.realestatemanager.database.RealEstateDatabase
import yofaraway.openclassrooms.realestatemanager.database.converter.ContentProviderConverters
import yofaraway.openclassrooms.realestatemanager.provider.EstateContentProvider
import java.io.IOException
import java.util.*

@RunWith(AndroidJUnit4::class)
class ContentProviderTest {


    private lateinit var db: RealEstateDatabase
    private lateinit var mContentResolver: ContentResolver


    @Before
    fun createDb() {
        db = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().context, RealEstateDatabase::class.java
        ).allowMainThreadQueries().build()
        mContentResolver = InstrumentationRegistry.getInstrumentation().context.contentResolver
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun getEstatesWhenNoEstateInserted() {
        val cursor = mContentResolver.query(
            ContentUris.withAppendedId(
                EstateContentProvider.URI_ESTATE,
                ESTATE_ID
            ), null, null, null, null
        )
        assertThat<Cursor>(cursor, notNullValue())
        assertThat<Int>(cursor?.count, `is`<Int>(1))
        cursor?.close()
    }

    @Test
    fun insertAndGetRealEstate() {
        // BEFORE : Adding demo item
        val id = mContentResolver.insert(EstateContentProvider.URI_ESTATE, generateEstate())


        val cursor = mContentResolver.query(id!!, null, null, null, null)
        assertThat<Cursor>(cursor, notNullValue())
        assertThat<Int>(cursor!!.count, `is`<Int>(1))
        assertThat<Boolean>(cursor.moveToFirst(), `is`<Boolean>(true))

        assertThat<String>(
            cursor.getString(cursor.getColumnIndexOrThrow("type")), `is`<String>("2")
        )
        assertThat<String>(
            cursor.getString(cursor.getColumnIndexOrThrow("priceDollars")),
            `is`<String>("1000000")
        )
        assertThat<String>(
            cursor.getString(cursor.getColumnIndexOrThrow("surface")),
            `is`<String>("450")
        )
        assertThat<String>(
            cursor.getString(cursor.getColumnIndexOrThrow("rooms")),
            `is`<String>("9")
        )
        assertThat<String>(
            cursor.getString(cursor.getColumnIndexOrThrow("bedrooms")),
            `is`<String>("5")
        )
        assertThat<String>(
            cursor.getString(cursor.getColumnIndexOrThrow("hasBeenSold")),
            `is`("0")
        )
    }

    @Test
    fun insertAndGetListOfString(){
        // -- BEFORE --
        // Create list
        val list = mutableListOf("Test1", "Test2")
        // Convert it to Json
        val listAsJson = ContentProviderConverters.listToJson(list)
        // Add it to the content values
        val estateValues = generateEstate()
        estateValues.put("photosTitlesList", listAsJson)
        // Add it to the content provider
        val id = mContentResolver.insert(EstateContentProvider.URI_ESTATE, estateValues)

        // -- THEN --
        // Get estate
        val cursor = mContentResolver.query(id!!, null, null, null, null)
        assertThat<Cursor>(cursor, notNullValue())
        assertThat<Int>(cursor!!.count, `is`<Int>(1))
        assertThat<Boolean>(cursor.moveToFirst(), `is`<Boolean>(true))
        // Convert data back to a list and test it
        val jsonToList = ContentProviderConverters.jsonToList(cursor.getString(cursor.getColumnIndexOrThrow("photosTitlesList")))
        assertEquals(list, jsonToList)
        assertEquals("Test2", jsonToList[1])

    }

    @Test
    fun insertAndGetDate(){
        // -- BEFORE --
        // Create Date
        val dateTest = Date()
        // Convert it to Long
        val dateAsLong = ContentProviderConverters.dateToLong(dateTest)
        // Add it to the content values
        val estateValues = generateEstate()
        estateValues.put("dateAvailable", dateAsLong)
        // Add it to the content provider
        val id = mContentResolver.insert(EstateContentProvider.URI_ESTATE, estateValues)

        // -- THEN --
        // Get estate
        val cursor = mContentResolver.query(id!!, null, null, null, null)
        assertThat<Cursor>(cursor, notNullValue())
        assertThat<Int>(cursor!!.count, `is`<Int>(1))
        assertThat<Boolean>(cursor.moveToFirst(), `is`<Boolean>(true))
        // Convert data back to a date and test it
        val longToDate = ContentProviderConverters.longToDate(cursor.getString(cursor.getColumnIndexOrThrow("dateAvailable")).toLong())
        assertEquals(dateTest, longToDate)
    }



    companion object {
        const val ESTATE_ID: Long = 1

        fun generateEstate(): ContentValues {
            val values = ContentValues()
            values.put("id", "1")
            values.put("type", "2")
            values.put("priceDollars", "1000000")
            values.put("surface", "450")
            values.put("rooms", "9")
            values.put("bedrooms", "5")
            values.put("bathrooms", "2")
            values.put("description", "")
            values.put("photosPathList", getJsonTest())
            values.put("photosTitlesList", getJsonTest())
            values.put("address", "")
            values.put("latLng", getJsonTest())
            values.put("placesNear", getJsonTest())
            values.put("dateAvailable", getLongDateTest())
            values.put("hasBeenSold", false)
            values.put("dateSold", getLongDateTest())
            values.put("agent", "John")
            return values
        }

        private fun getJsonTest(): String {
            return ContentProviderConverters.listToJson(mutableListOf("test"))
        }

        private fun getLongDateTest(): Long {
            return ContentProviderConverters.dateToLong(Date())
        }

    }

}