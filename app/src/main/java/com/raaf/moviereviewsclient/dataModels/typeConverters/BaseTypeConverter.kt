package com.raaf.moviereviewsclient.dataModels.typeConverters

abstract class BaseTypeConverter<T> {

    abstract fun fromObject(dataObject: T) : String

    abstract fun toObject(string: String) : T

    protected fun convertNull(param: String) : String? {
        return if (param.contains(STRING_NULL)) null
        else param
    }

    companion object {
        const val STRING_DIVIDER = " ,   "
        const val STRING_NULL = "null"
    }
}