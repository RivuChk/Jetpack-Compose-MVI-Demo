package dev.rivu.mvijetpackcomposedemo.moviesearch.data.remote

import dev.rivu.mvijetpackcomposedemo.moviesearch.data.remote.model.MovieSearchResponse
import dev.rivu.mvijetpackcomposedemo.BuildConfig
import dev.rivu.mvijetpackcomposedemo.moviesearch.data.remote.model.MovieDetailResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieApi {

    @GET("/")
    fun searchMovies(
        @Query("s") query: String,
        @Query("type") type: String = "movie",
        @Query("apikey") apikey: String = BuildConfig.ApiKey
    ): Single<MovieSearchResponse>

    @GET("/")
    fun getMovieDetail(
        @Query("i") imdbId: String,
        @Query("apikey") apikey: String = BuildConfig.ApiKey
    ): Single<MovieDetailResponse>
}