package yofaraway.openclassrooms.realestatemanager

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.jraska.livedata.test
import org.junit.*
import org.junit.runner.RunWith
import yofaraway.openclassrooms.realestatemanager.database.RealEstateDatabase
import yofaraway.openclassrooms.realestatemanager.database.dao.EstateDao
import yofaraway.openclassrooms.realestatemanager.model.Estate
import java.io.IOException
import java.util.*


@RunWith(AndroidJUnit4::class)
class DatabaseTest {
    //to test LiveData
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var estateDao: EstateDao
    private lateinit var db: RealEstateDatabase

    @Before
    fun createDb() {
        db = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().context, RealEstateDatabase::class.java
        ).allowMainThreadQueries().build()
        estateDao = db.estateDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun writeEstateAndReadInList() {

        val estate = getFakeEstateOne()

        val id = estateDao.insertEstate(estate)
        val byId = estateDao.getEstateWithId(id)
        byId.test().awaitValue()
        Assert.assertEquals(estate.description, byId.value?.description)
    }

    @Test
    @Throws(Exception::class)
    fun updateEstateAndReadInList() {

        val estate = getFakeEstateOne()

        // Insert it in db then get it from db
        val id = estateDao.insertEstate(estate)
        var byId = estateDao.getEstateWithId(id)
        byId.test().awaitValue()

        // Edit description and update the db
        val newDescription = "New description updated"
        byId.value!!.description = newDescription
        byId.test().awaitValue()
        estateDao.updateEstate(byId.value!!)

        // Read it from db
        byId = estateDao.getEstateWithId(id)
        byId.test().awaitValue()
        Assert.assertEquals(newDescription, byId.value?.description)


    }

    @Test
    @Throws(Exception::class)
    fun getEstatesList() {

        // Given list in db is empty
        var estates = estateDao.getEstates()
        estates.test().awaitValue()
        Assert.assertTrue(estates.value?.size == 0)

        // When we add 2 items
        estateDao.insertEstate(getFakeEstateOne())
        estateDao.insertEstate(getFakeEstateTwo())

        // Then the list has now 2 items
        estates = estateDao.getEstates()
        estates.test().awaitValue()
        Assert.assertTrue(estates.value?.size == 2)
        
    }


    companion object {
        private fun getFakeEstateOne(): Estate {
            return Estate(
                null,
                "Flat",
                100000,
                99,
                5,
                4,
                3,
                "For testing",
                mutableListOf("nullPath/test"),
                mutableListOf("(no title)"),
                "12 street of Test-City-77777",
                mutableListOf(),
                mutableListOf(),
                false,
                Date(),
                null,
                "John Smith"
            )

        }

        private fun getFakeEstateTwo(): Estate {
            return Estate(
                null,
                "House",
                12,
                25,
                1,
                1,
                1,
                "For testing 2",
                mutableListOf("nullPath/test"),
                mutableListOf("(no title)"),
                "13 street of Test-City-77777",
                mutableListOf(),
                mutableListOf(),
                false,
                Date(),
                null,
                "Laura Smith"
            )

        }
    }


}

