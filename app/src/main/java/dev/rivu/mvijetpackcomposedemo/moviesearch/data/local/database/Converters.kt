package dev.rivu.mvijetpackcomposedemo.moviesearch.data.local.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    val gson by lazy {
        Gson()
    }

    @TypeConverter
    fun fromListOfStringToString(list: List<String>): String {
        return list.joinToString(separator = "|")
    }

    @TypeConverter
    fun fromStringToListOfString(str: String): List<String> {
        return str.split("|")
    }

    @TypeConverter
    fun fromListOfRatingToString(list: List<MovieEnitity.Detail.Rating>): String {
        return gson.toJson(list)
    }

    @TypeConverter
    fun fromStringToListOfRating(str: String): List<MovieEnitity.Detail.Rating> {
        val listType = object: TypeToken<List<MovieEnitity.Detail.Rating>>() {}.type
        return gson.fromJson(str, listType)
    }
}