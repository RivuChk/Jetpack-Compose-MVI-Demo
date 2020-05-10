package dev.rivu.mvijetpackcomposedemo.moviesearch.data.local

import androidx.room.EmptyResultSetException
import dev.rivu.mvijetpackcomposedemo.moviesearch.data.MovieDataStore
import dev.rivu.mvijetpackcomposedemo.moviesearch.data.local.database.MovieDao
import dev.rivu.mvijetpackcomposedemo.moviesearch.data.local.database.MovieEnitity
import dev.rivu.mvijetpackcomposedemo.moviesearch.data.model.Movie
import dev.rivu.mvijetpackcomposedemo.moviesearch.data.model.MovieDetail
import hu.akarnokd.rxjava3.bridge.RxJavaBridge
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single

class LocalMovieDataStore(private val movieDao: MovieDao) : MovieDataStore {

    override fun getMoviesStream(searchQuery: String): Flowable<List<Movie>> {
        return RxJavaBridge.toV3Flowable(
            movieDao.getMoviesStream(searchQuery)
                .map { list ->
                    list.map {
                        Movie(
                            imdbID = it.imdbID,
                            poster = it.poster,
                            title = it.title,
                            type = it.type,
                            year = it.year
                        )
                    }
                }
        )
    }

    override fun getMovies(searchQuery: String): Single<List<Movie>> {
        return RxJavaBridge.toV3Single(
            movieDao.getMovies(searchQuery)
                .map { list ->
                    list.map {
                        Movie(
                            imdbID = it.imdbID,
                            poster = it.poster,
                            title = it.title,
                            type = it.type,
                            year = it.year
                        )
                    }
                }
        )
    }

    override fun addMovies(movieList: List<Movie>): Completable {
        return return RxJavaBridge.toV3Completable(
            movieDao.addMovies(
                movieList.map {
                    MovieEnitity(
                        imdbID = it.imdbID,
                        poster = it.poster,
                        title = it.title,
                        type = it.type,
                        year = it.year
                    )
                }
            )
        )
    }

    override fun getMovieDetail(imdbId: String): Single<MovieDetail> {
        return RxJavaBridge.toV3Single(
            movieDao.getMovie(imdbId)
                .map {
                    if (it.detail == null) {
                        throw EmptyResultSetException("detail not present for this movie")
                    } else {
                        MovieDetail(
                            imdbID = it.imdbID,
                            poster = it.poster,
                            title = it.title,
                            type = it.type,
                            year = it.year,

                            response = it.detail.response,
                            actors = it.detail.actors,
                            awards = it.detail.awards,
                            boxOffice = it.detail.boxOffice,
                            country = it.detail.country,
                            dVD = it.detail.dVD,
                            director = it.detail.director,
                            genre = it.detail.genre,
                            imdbRating = it.detail.imdbRating,
                            imdbVotes = it.detail.imdbVotes,
                            language = it.detail.language,
                            metascore = it.detail.metascore,
                            plot = it.detail.plot,
                            production = it.detail.production,
                            rated = it.detail.rated,
                            ratings = it.detail.ratings.map {
                                MovieDetail.Rating(
                                    source = it.source,
                                    value = it.value
                                )
                            },
                            released = it.detail.released,
                            runtime = it.detail.runtime,
                            website = it.detail.website,
                            writer = it.detail.writer
                        )
                    }
                }
        )
    }

    override fun addMovieDetail(movie: MovieDetail): Completable {
        return RxJavaBridge.toV3Completable(
            movieDao.updateMovieInDB(
                MovieEnitity(
                    imdbID = movie.imdbID,
                    poster = movie.poster,
                    title = movie.title,
                    type = movie.type,
                    year = movie.year,
                    detail = MovieEnitity.Detail(
                        response = movie.response,
                        actors = movie.actors,
                        awards = movie.awards,
                        boxOffice = movie.boxOffice,
                        country = movie.country,
                        dVD = movie.dVD,
                        director = movie.director,
                        genre = movie.genre,
                        imdbRating = movie.imdbRating,
                        imdbVotes = movie.imdbVotes,
                        language = movie.language,
                        metascore = movie.metascore,
                        plot = movie.plot,
                        production = movie.production,
                        rated = movie.rated,
                        ratings = movie.ratings.map {
                            MovieEnitity.Detail.Rating(
                                source = it.source,
                                value = it.value
                            )
                        },
                        released = movie.released,
                        runtime = movie.runtime,
                        website = movie.website,
                        writer = movie.writer
                    )
                )
            )
        )
    }
}