package com.raaf.moviereviewsclient.dataModels.typeConverters

import androidx.room.TypeConverter
import com.raaf.moviereviewsclient.dataModels.Link

class LinkTypeConverter : BaseTypeConverter<Link>() {

    @TypeConverter
    override fun fromObject(dataObject: Link): String {
        return "${dataObject.type}$STRING_DIVIDER" +
                "${dataObject.reviewUrl}$STRING_DIVIDER${dataObject.suggest}$"
    }

    @TypeConverter
    override fun toObject(string: String): Link {
        var splitString = string.split(STRING_DIVIDER)
        return Link(
            convertNull(splitString[0]),
            convertNull(splitString[1]),
            convertNull(splitString[2]))
    }
}