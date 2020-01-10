package yofaraway.openclassrooms.realestatemanager.ui.update

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import yofaraway.openclassrooms.realestatemanager.model.Estate
import yofaraway.openclassrooms.realestatemanager.utils.Utils.convertDollarToEuro
import yofaraway.openclassrooms.realestatemanager.utils.Utils.convertEuroToDollar
import yofaraway.openclassrooms.realestatemanager.utils.Utils.getAddress
import yofaraway.openclassrooms.realestatemanager.utils.Utils.getCity
import yofaraway.openclassrooms.realestatemanager.utils.Utils.getZipCode
import java.util.*

class UpdateEstateViewModel : ViewModel() {

    var estate: Estate? = null

    // INPUTS RECEIVERS (2 ways data-binding)

    // EDIT TEXT
    val type = MutableLiveData<String>()
    val surface = MutableLiveData<String>()
    val price = MutableLiveData<String>()
    val rooms = MutableLiveData<String>()
    val bathrooms = MutableLiveData<String>()
    val bedrooms = MutableLiveData<String>()
    val address = MutableLiveData<String>()
    val addressCity = MutableLiveData<String>()
    val addressZipCode = MutableLiveData<String>()
    val agent = MutableLiveData<String>()
    val description = MutableLiveData<String>()
    // STATUS
    val hasBeenSold = MutableLiveData<Boolean>(false)
    val status = MutableLiveData<String>()
    // DATE PICKERS
    val dateAvailable = MutableLiveData<Date>()
    val dateSold = MutableLiveData<Date>()
    // NEAR PLACES (CHIPS & CHECKBOXES)
    var nearPlaces = MutableLiveData<List<String?>>(listOf())
    lateinit var placesChoicesCheckedList: BooleanArray
    // PHOTOS
    var photoPathList = MutableLiveData<MutableList<String>>()
    var photoTitleList = MutableLiveData<MutableList<String>>()
    val atLeastOnePhoto = MutableLiveData<Boolean>(false)
    var newPhotoPath: String? = null

    // CHECK INPUTS
    val showError = MutableLiveData<Boolean>(false)
    val updateEstate = MutableLiveData<Boolean>(false)

    // CURRENCY
    var currency: String? = "Dollar"

    fun initCurrency(currentCurrency: String) {
        currency = currentCurrency
    }

    fun init(estate: Estate, strAvailable: String, strSold: String, listNearPlaces: Array<String>) {
        this.estate = estate
        type.value = estate.type
        surface.value = estate.surface.toString()
        rooms.value = estate.rooms.toString()
        bathrooms.value = estate.bathrooms.toString()
        bedrooms.value = estate.bedrooms.toString()
        address.value = getAddress(estate.address)
        addressCity.value = getCity(estate.address)
        addressZipCode.value = getZipCode(estate.address)
        agent.value = estate.agent
        description.value = estate.description
        dateAvailable.value = estate.dateAvailable
        nearPlaces.value = estate.placesNear
        placesChoicesCheckedList = BooleanArray(listNearPlaces.size)
        photoPathList.value = estate.photosPathList.toMutableList()
        photoTitleList.value = estate.photosTitlesList.toMutableList()

        if (currency == "Euro") price.value = convertDollarToEuro(estate.priceDollars).toString()
        else price.value = estate.priceDollars.toString()


        // for each places in the list, set true for this place index in the list of booleans
        for (strPlace in estate.placesNear) {
            placesChoicesCheckedList[listNearPlaces.indexOf(strPlace)] = true
        }

        // Date sold
        if (estate.dateSold != null) {
            hasBeenSold.value = true
            dateSold.value = estate.dateSold
            status.value = strSold
        } else {
            hasBeenSold.value = false
            status.value = strAvailable
        }
    }


    // on button click (listener with data binding)
    fun onConfirmBtnClick() {
        if (!checkEmptyEditTexts(getStringInputs())
            && !checkEmptyDates(getDateInputs())
            && atLeastOnePhoto.value!!
        ) createNewUpdatedEstate()
        else showError.value = true
    }


    private fun getStringInputs(): MutableSet<String?> {
        return mutableSetOf(
            type.value,
            price.value,
            surface.value,
            rooms.value,
            bathrooms.value,
            bedrooms.value,
            description.value,
            address.value,
            addressCity.value,
            addressZipCode.value,
            agent.value
        )

    }


    private fun getDateInputs(): MutableSet<Date?> {
        val inputs = mutableSetOf(
            dateAvailable.value
        )
        if (hasBeenSold.value!!) inputs.add(dateSold.value)
        return inputs
    }


    private fun checkEmptyEditTexts(inputs: MutableSet<String?>): Boolean {
        return (inputs.any { it == null || it == "" })
    }


    private fun checkEmptyDates(inputs: MutableSet<Date?>): Boolean {
        return (inputs.any { it == null })
    }


    private fun createNewUpdatedEstate() {
        val addressComplete = "${address.value}|${addressCity.value}|${addressZipCode.value}"
        if (!hasBeenSold.value!!) dateSold.value = null
        if (currency == "Euro") price.value = convertEuroToDollar(price.value!!.toInt()).toString()

        estate.apply {
            this?.type = type.value!!
            this?.priceDollars = price.value!!.toInt()
            this?.surface = surface.value!!.toInt()
            this?.rooms = rooms.value!!.toInt()
            this?.bathrooms = bathrooms.value!!.toInt()
            this?.bedrooms = bedrooms.value!!.toInt()
            this?.description = description.value!!
            this?.photosPathList = photoPathList.value!!.toList()
            this?.photosTitlesList = photoTitleList.value!!.toList()
            this?.address = addressComplete
            this?.latLng = mutableListOf()
            this?.placesNear = nearPlaces.value!!
            this?.hasBeenSold = hasBeenSold.value!!
            this?.dateAvailable = dateAvailable.value!!
            this?.dateSold = dateSold.value
            this?.agent = agent.value!!
        }
        updateEstate.value = true
    }
}

