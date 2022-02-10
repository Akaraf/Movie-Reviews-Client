package com.raaf.moviereviewsclient.dataModels.typeConverters

import androidx.room.TypeConverter
import com.raaf.moviereviewsclient.dataModels.MultiMedia

class MultiMediaTypeConverter : BaseTypeConverter<MultiMedia>() {

    @TypeConverter
    override fun fromObject(dataObject: MultiMedia): String {
        return "${dataObject.imageName}$STRING_DIVIDER${dataObject.url}"
    }

    @TypeConverter
    override fun toObject(string: String): MultiMedia {
        var  splitString = string.split(STRING_DIVIDER)
        return MultiMedia(
            splitString[0],
            convertNull(splitString[1])
        )
    }
}