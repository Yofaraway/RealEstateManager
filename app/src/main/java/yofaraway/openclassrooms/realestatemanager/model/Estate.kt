package yofaraway.openclassrooms.realestatemanager.model

import androidx.room.Entity
import androidx.room.PrimaryKey
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


)

