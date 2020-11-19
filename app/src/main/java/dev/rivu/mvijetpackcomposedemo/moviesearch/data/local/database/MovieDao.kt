package dev.rivu.mvijetpackcomposedemo.moviesearch.data.local.database

import androidx.room.*
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single

@Dao
interface MovieDao {
    @Query("select * from movies where title like '%' || :searchQuery || '%'")
    fun getMoviesStream(searchQuery: String): Flowable<List<MovieEnitity>>

    @Query("select * from movies where title like '%' || :searchQuery || '%'")
    fun getMovies(searchQuery: String): Single<List<MovieEnitity>>

    @Query("select * from movies where imdbID = :imdbID")
    fun getMovie(imdbID: String): Single<MovieEnitity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addMovies(movieList: List<MovieEnitity>): Completable

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateMovieInDB(movie: MovieEnitity): Completable
}