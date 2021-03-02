package dev.rivu.mvijetpackcomposedemo.moviesearch.data

import androidx.room.rxjava3.EmptyResultSetException
import dev.rivu.mvijetpackcomposedemo.moviesearch.data.model.Movie
import dev.rivu.mvijetpackcomposedemo.moviesearch.data.model.MovieDetail
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single

class MovieRepository(
    private val localDataStore: MovieDataStore,
    private val remoteDataStore: MovieDataStore
) : IMovieRepository {
    override fun getMovies(searchQuery: String): Flowable<List<Movie>> {
        return localDataStore.getMovies(searchQuery)
            .map {
                if (it.isNullOrEmpty()) {
                    throw EmptyResultSetException("Movies matching the query isn't in db")
                } else {
                    it
                }
            }
            .mergeWith(syncMovieSearchResult(searchQuery))
            .onErrorResumeNext {
                syncMovieSearchResult(searchQuery)
                    .toFlowable()
            }
    }

    override fun syncMovieSearchResult(searchQuery: String): Single<List<Movie>> {
        return remoteDataStore.getMovies(searchQuery)
            .flatMap { movies ->
                if (movies.isNotEmpty()) {
                    addMovies(movies)
                        .andThen(localDataStore.saveSearchHistory(searchQuery))
                        .andThen(
                            Single.just(movies)
                        )
                } else {
                    Single.error(EmptyResultSetException("No data found in Remote"))
                }
            }
    }

    override fun addMovies(movieList: List<Movie>): Completable {
        return localDataStore.addMovies(movieList)
    }

    override fun getMovieDetail(imdbId: String): Flowable<MovieDetail> {
        return localDataStore.getMovieDetail(imdbId)
            .flatMapPublisher { localMovieDetail ->
                Flowable.just(localMovieDetail)
                    .mergeWith(
                        remoteDataStore.getMovieDetail(imdbId)
                            .flatMap { remoteMovieDetail ->
                                localDataStore.addMovieDetail(remoteMovieDetail)
                                    .andThen(Single.just(remoteMovieDetail))
                            }
                            .onErrorComplete()
                    )
            }
            .onErrorResumeNext {
                remoteDataStore.getMovieDetail(imdbId)
                    .flatMap { remoteMovieDetail ->
                        localDataStore.addMovieDetail(remoteMovieDetail)
                            .andThen(Single.just(remoteMovieDetail))
                    }
                    .toFlowable()
            }
    }

    override fun saveSearchResult(list: List<String>): Completable {
        return localDataStore.saveSearchHistory(list = list)
    }

    override fun getSearchHistory(): Single<List<String>> {
        return localDataStore.getSearchHistory()
    }
}