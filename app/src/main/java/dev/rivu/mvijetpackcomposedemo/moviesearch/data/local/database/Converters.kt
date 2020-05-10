package dev.rivu.mvijetpackcomposedemo.moviesearch.data.local.database

import androidx.room.TypeConverter
import kotlinx.serialization.builtins.list
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration

class Converters {
    val json by lazy {
        Json(JsonConfiguration.Stable)
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
        return json.stringify(MovieEnitity.Detail.Rating.serializer().list, list)
    }

    @TypeConverter
    fun fromStringToListOfRating(str: String): List<MovieEnitity.Detail.Rating> {
        return json.parse(MovieEnitity.Detail.Rating.serializer().list, str)
    }
}