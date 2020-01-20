package yofaraway.openclassrooms.realestatemanager.provider

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import androidx.annotation.Nullable
import yofaraway.openclassrooms.realestatemanager.database.RealEstateDatabase
import yofaraway.openclassrooms.realestatemanager.model.Estate


class EstateContentProvider : ContentProvider() {

    override fun onCreate(): Boolean {
        return true
    }


    @Nullable
    override fun query(uri: Uri, @Nullable projection: Array<String?>?, @Nullable selection: String?, @Nullable selectionArgs: Array<String?>?, @Nullable sortOrder: String?): Cursor? {
        if (context != null) {
            val index = ContentUris.parseId(uri)
            val cursor: Cursor =
                RealEstateDatabase.getInstance(context!!).estateDao().getEstatesWithCursor(index)

            cursor.setNotificationUri(context!!.contentResolver, uri)
            return cursor
        }

        throw IllegalArgumentException("Failed to query row for uri $uri")
    }

    @Nullable
    override fun getType(uri: Uri): String? {
        return "vnd.android.cursor.item/$AUTHORITY.$TABLE_NAME"
    }

    @Nullable
    override fun insert(uri: Uri, @Nullable contentValues: ContentValues?): Uri? {
        if (context != null) {
            val id = RealEstateDatabase.getInstance(context!!).estateDao()
                .insertEstate(Estate().fromContentValues(contentValues))
            if (id != 0L) {
                context!!.contentResolver.notifyChange(uri, null)
                return ContentUris.withAppendedId(uri, id)
            }
        }
        throw IllegalArgumentException("Failed to insert row into $uri")
    }

    override fun delete(uri: Uri, @Nullable s: String?, @Nullable strings: Array<String?>?): Int {
        if (context != null) {
            val count = RealEstateDatabase.getInstance(context!!).estateDao().deleteEstate(ContentUris.parseId(uri))
            context!!.contentResolver.notifyChange(uri, null)
            return count
        }
        throw IllegalArgumentException("Failed to delete row into $uri")
    }

    override fun update(uri: Uri, @Nullable contentValues: ContentValues?, @Nullable s: String?, @Nullable strings: Array<String?>?): Int {
        if (context != null) {
            val count = RealEstateDatabase.getInstance(context!!).estateDao().updateEstate(Estate().fromContentValues(contentValues!!))
            context!!.contentResolver.notifyChange(uri, null)
            return count
        }
        throw IllegalArgumentException("Failed to update row into $uri")
    }

    companion object {

        const val AUTHORITY = "yofaraway.openclassrooms.realestatemanager.provider"
        val TABLE_NAME = Estate::class.java.simpleName
        val URI_ESTATE = Uri.parse("content://$AUTHORITY/$TABLE_NAME")
    }
}