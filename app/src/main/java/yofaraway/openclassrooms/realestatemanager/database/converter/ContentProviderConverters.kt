package yofaraway.openclassrooms.realestatemanager.database.converter

import com.google.gson.Gson
import java.util.*

class ContentProviderConverters {
    companion object {
        fun dateToLong(date: Date): Long {
            return date.time
        }


        fun longToDate(dateLong: Long): Date {
            return Date(dateLong)
        }

        fun listToJson(value: List<String>?): String {
            return Gson().toJson(value)
        }

        fun jsonToListNN(value: String): List<String> {
            val objects = Gson().fromJson(value, Array<String>::class.java)
            return objects.toList()
        }

        fun jsonToList(value: String?): List<String?> {
            val objects = Gson().fromJson(value, Array<String?>::class.java)
            return objects.toList()
        }
    }
}