package dev.rivu.mvijetpackcomposedemo.moviesearch.data.local

import androidx.room.EmptyResultSetException
import dev.rivu.mvijetpackcomposedemo.moviesearch.data.MovieDataStore
import dev.rivu.mvijetpackcomposedemo.moviesearch.data.local.database.MovieDao
import dev.rivu.mvijetpackcomposedemo.moviesearch.data.model.Movie
import dev.rivu.mvijetpackcomposedemo.moviesearch.data.model.MovieDetail
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single

class LocalMovieDataStore(private val movieDao: MovieDao) : MovieDataStore {

    //Not Implementing DB (Room DB) here as Jetpack Compose has some conflicts with kapt
    //once that's resolved, we will implement Room DB here

    val movies: MutableSet<Movie> = mutableSetOf()
    val movieDetails: MutableMap<String, MovieDetail> = mutableMapOf()

    override fun getMoviesStream(searchQuery: String): Flowable<List<Movie>> {
        return Single.defer<List<Movie>> {
            Single.just<List<Movie>>(
                movies.filter {
                    it.title.contains(searchQuery, ignoreCase = true)
                }
            )
        }.toFlowable()
    }

    override fun getMovies(searchQuery: String): Single<List<Movie>> {
        return Single.defer<List<Movie>> {
            Single.just<List<Movie>>(
                movies.filter {
                    it.title.contains(searchQuery, ignoreCase = true)
                }
            )
        }
    }

    override fun addMovies(movieList: List<Movie>): Completable {
        return Completable.fromCallable {
            movies.addAll(movieList)
        }
    }

    override fun getMovieDetail(imdbId: String): Single<MovieDetail> {
        return Single.defer {
            if (movieDetails.containsKey(imdbId)) {
                Single.just(movieDetails.get(imdbId))
            } else {
                Single.error(EmptyResultSetException("Movie not found in DB"))
            }
        }
    }

    override fun addMovieDetail(movie: MovieDetail): Completable {
        return Completable.fromAction {
            movieDetails[movie.imdbID] = movie
        }
    }
}