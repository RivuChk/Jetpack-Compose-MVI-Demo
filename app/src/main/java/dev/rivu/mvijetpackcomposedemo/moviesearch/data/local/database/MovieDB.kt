package dev.rivu.mvijetpackcomposedemo.moviesearch.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [MovieEnitity::class], version = 1)
@TypeConverters(Converters::class)
abstract class MovieDB: RoomDatabase() {
    abstract fun movieDao(): MovieDao

    companion object {
        private var instance: MovieDB? = null

        @JvmStatic @Synchronized
        fun getInstance(applicationContext: Context): MovieDB {
            if (instance == null) {
                instance = Room.databaseBuilder(applicationContext, MovieDB::class.java, "movie_search.db").build()
            }

            return instance!!
        }
    }
}