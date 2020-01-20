package yofaraway.openclassrooms.realestatemanager.model

import android.content.ContentValues
import android.util.Log
import androidx.room.Entity
import androidx.room.PrimaryKey
import yofaraway.openclassrooms.realestatemanager.database.converter.Converters
import java.util.*


@Entity(tableName = "estates")
data class Estate(
    @PrimaryKey(autoGenerate = true) var id: Long?,
    var type: String,
    var priceDollars: Int,
    var surface: Int,
    var rooms: Int,
    var bathrooms: Int,
    var bedrooms: Int,
    var description: String,
    var photosPathList: List<String>,
    var photosTitlesList: List<String>,
    var address: String,
    var latLng: List<String?>,
    var placesNear: List<String?>,
    var hasBeenSold: Boolean,
    var dateAvailable: Date,
    var dateSold: Date?,
    var agent: String


) {

    constructor() : this(
        null, "", 0, 0, 0,
        0, 0, "", mutableListOf(""), mutableListOf(""),
        "", mutableListOf(), mutableListOf(), false, Date(), null, ""
    )

    fun fromContentValues(values: ContentValues): Estate {
        val estate = Estate()
        if (values.containsKey("type")) estate.type = values.getAsString("type")
        if (values.containsKey("priceDollars")) estate.priceDollars =
            values.getAsInteger("priceDollars")
        if (values.containsKey("surface")) estate.surface = values.getAsInteger("surface")
        if (values.containsKey("rooms")) estate.rooms = values.getAsInteger("rooms")
        if (values.containsKey("bathrooms")) estate.bathrooms = values.getAsInteger("bathrooms")
        if (values.containsKey("bedrooms")) estate.bedrooms = values.getAsInteger("bedrooms")
        if (values.containsKey("description")) estate.description =
            values.getAsString("description")

        var photosPathJson = Converters.jsonToListNN(values.getAsString("photosPathList"))
        if (photosPathJson.isNullOrEmpty()) photosPathJson = mutableListOf("")
        if (values.containsKey("photosPathList")) estate.photosPathList = photosPathJson

        var photosTitlesJson = Converters.jsonToListNN(values.getAsString("photosTitlesList"))
        if (photosTitlesJson.isNullOrEmpty()) photosTitlesJson = mutableListOf("")
        if (values.containsKey("photosTitlesList")) estate.photosTitlesList = photosTitlesJson

        if (values.containsKey("address")) estate.address = values.getAsString("address")

        var latLngJson = Converters.jsonToListNN(values.getAsString("latLng"))
        if (latLngJson.isNullOrEmpty()) latLngJson = mutableListOf()
        if (values.containsKey("latLng")) estate.latLng = latLngJson

        var placesNear = Converters.jsonToListNN(values.getAsString("placesNear"))
        if (placesNear.isNullOrEmpty()) placesNear = mutableListOf()
        if (values.containsKey("placesNear")) estate.placesNear = placesNear

        if (values.containsKey("hasBeenSold")) estate.hasBeenSold =
            values.getAsBoolean("hasBeenSold")

        var longDateAvailable = Converters.toDate(values.getAsLong("dateAvailable"))
        if (longDateAvailable == null) longDateAvailable = Date()
        if (values.containsKey("dateAvailable")) estate.dateAvailable = longDateAvailable

        val longDateSold = Converters.toDate(values.getAsLong("dateSold"))
        if (estate.hasBeenSold) if (values.containsKey("dateSold")) estate.dateSold = longDateSold

        if (values.containsKey("agent")) estate.agent = values.getAsString("agent")
        Log.e("EstateFromContentValues", "Estate : $estate")
        return estate
    }
}




