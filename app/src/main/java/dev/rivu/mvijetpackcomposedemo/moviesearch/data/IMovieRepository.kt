package dev.rivu.mvijetpackcomposedemo.moviesearch.data

import dev.rivu.mvijetpackcomposedemo.moviesearch.data.model.Movie
import dev.rivu.mvijetpackcomposedemo.moviesearch.data.model.MovieDetail
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single

interface IMovieRepository {
    fun getMovies(searchQuery: String): Flowable<List<Movie>>

    fun addMovies(movieList: List<Movie>): Completable

    fun syncMovieSearchResult(searchQuery: String): Single<List<Movie>>

    fun getMovieDetail(imdbId: String): Single<MovieDetail>
}