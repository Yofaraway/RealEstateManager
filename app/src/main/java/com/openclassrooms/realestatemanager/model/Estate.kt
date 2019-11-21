package com.openclassrooms.realestatemanager.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "estates")
data class Estate(
    @PrimaryKey(autoGenerate = true) var id: Long?,
    var type: String,
    var priceDollars: Int,
    var surface: Double,
    var rooms: Int,
    var bathrooms: Int,
    var bedrooms: Int,
    var description: String,
    var pathPhotos: List<String>,
    var titlesPhotos: List<String>,
    var address: String,
    var nearTo: String?,
    var hasBeenSold: Boolean,
    var dateAvailableSince: Date,
    var dateSold: Date?,
    var agent: String


)

