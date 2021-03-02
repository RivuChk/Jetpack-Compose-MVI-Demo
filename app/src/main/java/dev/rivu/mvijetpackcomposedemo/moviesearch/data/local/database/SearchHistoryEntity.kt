package dev.rivu.mvijetpackcomposedemo.moviesearch.data.local.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "searchHistory")
data class SearchHistoryEntity(
    @PrimaryKey
    val searchTerm: String,
    val timeStamp: String
)
