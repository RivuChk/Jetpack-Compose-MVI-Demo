package dev.rivu.mvijetpackcomposedemo.moviesearch.data.remote

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import dev.rivu.mvijetpackcomposedemo.moviesearch.data.datafactory.dummyMovieDetail
import dev.rivu.mvijetpackcomposedemo.moviesearch.data.datafactory.dummyMovieDetailResponse
import dev.rivu.mvijetpackcomposedemo.moviesearch.data.datafactory.dummyMovieSearchResponse
import dev.rivu.mvijetpackcomposedemo.moviesearch.data.model.Movie
import io.reactivex.rxjava3.core.Single
import org.mockito.ArgumentMatchers.anyString
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe


class RemoteMovieDataStoreSpecTest : Spek({

    val movieApi: MovieApi = mock()
    val remoteDataStore by memoized { RemoteMovieDataStore(movieApi) }

    describe("#${RemoteMovieDataStore::getMoviesStream.name}") {

        it("should return Flowable.error with UnsupportedOperationException") {
            val testObserver = remoteDataStore.getMoviesStream("").test()
            testObserver.assertError(UnsupportedOperationException::class.java)
        }
    }

    describe("#${RemoteMovieDataStore::getMovies.name}") {

        context("movieApi returns error") {

            val error = Exception("some error")

            beforeEachGroup {
                whenever(
                    movieApi.searchMovies(
                        query = anyString(),
                        type = anyString(),
                        apikey = anyString()
                    )
                )
                    .thenReturn(
                        Single.error(error)
                    )
            }

            it("should return the same error, wrapped in Single") {
                val testObserver = remoteDataStore.getMovies("").test()
                testObserver.assertError(error)
            }
        }

        context("movieApi returns data") {

            val dummyResponse = dummyMovieSearchResponse

            val dummyMovieList = dummyResponse.movies.map {
                Movie(
                    imdbID = it.imdbID,
                    poster = it.poster,
                    title = it.title,
                    type = it.type,
                    year = it.year
                )
            }

            beforeEachGroup {
                whenever(
                    movieApi.searchMovies(
                        query = anyString(),
                        type = anyString(),
                        apikey = anyString()
                    )
                )
                    .thenReturn(
                        Single.just(dummyResponse)
                    )
            }

            it("should return the same error, wrapped in Single") {
                val testObserver = remoteDataStore.getMovies("").test()
                testObserver.assertValue(dummyMovieList)
            }
        }
    }

    describe("#${RemoteMovieDataStore::addMovies.name}") {

        it("should return Completable.error with UnsupportedOperationException") {
            val testObserver = remoteDataStore.addMovies(listOf()).test()
            testObserver.assertError(UnsupportedOperationException::class.java)
        }
    }

    describe("#${RemoteMovieDataStore::getMovieDetail.name}") {

        context("movieApi returns error") {

            val error = Exception("some error")

            beforeEachGroup {
                whenever(movieApi.getMovieDetail(imdbId = anyString(), apikey = anyString()))
                    .thenReturn(
                        Single.error(error)
                    )
            }

            it("should return the same error, wrapped in Single") {
                val testObserver = remoteDataStore.getMovieDetail("").test()
                testObserver.assertError(error)
            }
        }

        context("movieApi returns data") {

            val dummyResponse = dummyMovieDetailResponse

            val dummyMovieDetail = dummyMovieDetail

            beforeEachGroup {
                whenever(movieApi.getMovieDetail(imdbId = anyString(), apikey = anyString()))
                    .thenReturn(
                        Single.just(dummyResponse)
                    )
            }

            it("should return the same error, wrapped in Single") {
                val testObserver = remoteDataStore.getMovieDetail("").test()
                testObserver.assertValue(dummyMovieDetail)
            }
        }
    }

    describe("#${RemoteMovieDataStore::addMovieDetail.name}") {

        it("should return Completable.error with UnsupportedOperationException") {
            val testObserver = remoteDataStore.addMovies(listOf()).test()
            testObserver.assertError(UnsupportedOperationException::class.java)
        }
    }
})