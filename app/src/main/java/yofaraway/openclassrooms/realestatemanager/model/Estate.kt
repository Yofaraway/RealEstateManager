package yofaraway.openclassrooms.realestatemanager.model

import android.content.ContentValues
import android.util.Log
import androidx.room.Entity
import androidx.room.PrimaryKey
import yofaraway.openclassrooms.realestatemanager.database.converter.ContentProviderConverters.Companion.jsonToList
import yofaraway.openclassrooms.realestatemanager.database.converter.ContentProviderConverters.Companion.jsonToListNN
import yofaraway.openclassrooms.realestatemanager.database.converter.ContentProviderConverters.Companion.longToDate
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

    fun fromContentValues(values: ContentValues?): Estate {
        val estate = Estate()
        if (values != null) {
            if (values.containsKey("type")) estate.type = values.getAsString("type")
            if (values.containsKey("priceDollars")) estate.priceDollars =
                values.getAsInteger("priceDollars")
            if (values.containsKey("surface")) estate.surface = values.getAsInteger("surface")
            if (values.containsKey("rooms")) estate.rooms = values.getAsInteger("rooms")
            if (values.containsKey("bathrooms")) estate.bathrooms = values.getAsInteger("bathrooms")
            if (values.containsKey("bedrooms")) estate.bedrooms = values.getAsInteger("bedrooms")
            if (values.containsKey("description")) estate.description =
                values.getAsString("description")

            val photosPathJson = jsonToListNN(values.getAsString("photosPathList"))
            if (values.containsKey("photosPathList")) estate.photosPathList = photosPathJson

            val photosTitlesJson = jsonToListNN(values.getAsString("photosTitlesList"))
            if (values.containsKey("photosTitlesList")) estate.photosTitlesList = photosTitlesJson

            if (values.containsKey("address")) estate.address = values.getAsString("address")

            val latLngJson = jsonToList(values.getAsString("latLng"))
            if (values.containsKey("latLng")) estate.latLng = latLngJson

            val placesNear = jsonToList(values.getAsString("placesNear"))
            if (values.containsKey("placesNear")) estate.placesNear = placesNear

            if (values.containsKey("hasBeenSold")) estate.hasBeenSold =
                values.getAsBoolean("hasBeenSold")

            val longDateAvailable = longToDate(values.getAsLong("dateAvailable"))
            if (values.containsKey("dateAvailable")) estate.dateAvailable = longDateAvailable

            val longDateSold = longToDate(values.getAsLong("dateSold"))
            if (estate.hasBeenSold) if (values.containsKey("dateSold")) estate.dateSold =
                longDateSold

            if (values.containsKey("agent")) estate.agent = values.getAsString("agent")
        }
        Log.e("EstateFromContentValues", "Estate : $estate")
        return estate
    }
}




