import com.nhaarman.mockitokotlin2.*
import dev.rivu.mvijetpackcomposedemo.moviesearch.data.EmptyResultSetException
import dev.rivu.mvijetpackcomposedemo.moviesearch.data.MovieDataStore
import dev.rivu.mvijetpackcomposedemo.moviesearch.data.MovieRepository
import dev.rivu.mvijetpackcomposedemo.moviesearch.data.datafactory.dummyMovieDetail
import dev.rivu.mvijetpackcomposedemo.moviesearch.data.datafactory.dummyMovieSearchList
import dev.rivu.mvijetpackcomposedemo.moviesearch.data.model.Movie
import dev.rivu.mvijetpackcomposedemo.moviesearch.data.remote.model.MovieSearchResponse
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import org.mockito.ArgumentMatchers.anyList
import org.mockito.ArgumentMatchers.anyString

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

class MovieRepositorySpecTest : Spek({

    val localDataStore: MovieDataStore = mock()
    val remoteDataStore: MovieDataStore = mock()

    val movieRepository by memoized {
        MovieRepository(localDataStore, remoteDataStore)
    }

    describe("#${MovieRepository::getMovies.name}") {

        context("both DataStore emits empty data") {

            beforeEachGroup {
                whenever(localDataStore.getMovies(anyString()))
                    .thenReturn(Single.just(emptyList()))
                whenever(remoteDataStore.getMovies(anyString()))
                    .thenReturn(Single.just(emptyList()))
            }

            it("should emit EmptyResultSetException") {
                val testObserver = movieRepository.getMovies("").test()
                testObserver.assertError(EmptyResultSetException::class.java)
            }
        }

        context("localDataStore emits empty data") {

            val dummyData = dummyMovieSearchList

            beforeEachGroup {
                whenever(localDataStore.getMovies(anyString()))
                    .thenReturn(Single.just(emptyList()))

                whenever(localDataStore.addMovies(anyList<Movie>()))
                    .thenReturn(Completable.complete())

                whenever(remoteDataStore.getMovies(anyString()))
                    .thenReturn(Single.just(dummyData))
            }

            it("should emit only remote data") {
                val testObserver = movieRepository.getMovies("").test()
                testObserver.assertValueCount(1)
                testObserver.assertValue(dummyData)
            }
        }

        context("remoteDataStore emits empty data") {

            val dummyData = dummyMovieSearchList

            beforeEachGroup {
                whenever(localDataStore.getMovies(anyString()))
                    .thenReturn(Single.just(emptyList()))

                whenever(remoteDataStore.getMovies(anyString()))
                    .thenReturn(Single.just(dummyData))
            }

            it("should emit only local data") {
                val testObserver = movieRepository.getMovies("").test()
                testObserver.assertValueCount(1)
                testObserver.assertValue(dummyData)
                testObserver.assertNoErrors()
            }
        }

        context("both DataStore emits empty data") {

            val dummyLocalData = dummyMovieSearchList
            val dummyRemoteData = dummyMovieSearchList

            beforeEachGroup {
                whenever(localDataStore.getMovies(anyString()))
                    .thenReturn(Single.just(dummyLocalData))

                whenever(localDataStore.addMovies(anyList()))
                    .thenReturn(Completable.complete())

                whenever(remoteDataStore.getMovies(anyString()))
                    .thenReturn(Single.just(dummyRemoteData))
            }

            it("should emit both data") {
                val testObserver = movieRepository.getMovies("").test()
                testObserver.assertValueCount(2)
                testObserver.assertValues(dummyLocalData, dummyRemoteData)
                testObserver.assertNoErrors()
            }
        }
    }

    describe("#${MovieRepository::syncMovieSearchResult.name}") {

        context("remoteDataStore returns data") {

            val dummyData = dummyMovieSearchList

            beforeEachGroup {

                whenever(localDataStore.addMovies(dummyData))
                    .thenReturn(Completable.complete())

                whenever(remoteDataStore.getMovies(anyString()))
                    .thenReturn(Single.just(dummyData))
            }

            it("should call localDataStore.addMovies") {
                val testObserver = movieRepository.syncMovieSearchResult("").test()
                testObserver.assertValue(dummyData)
                testObserver.assertNoErrors()
                verify(localDataStore, atLeastOnce()).addMovies(dummyData)
            }
        }
    }

    describe("#${MovieRepository::addMovies.name}") {

        context("localDataStore.addMovies completes") {

            beforeEachGroup {
                whenever(localDataStore.addMovies(anyList()))
                    .thenReturn(Completable.complete())
            }

            it("should complete") {
                val testObserver = movieRepository.addMovies(emptyList()).test()
                testObserver.assertComplete()
            }
        }

        context("localDataStore.addMovies emits error") {

            val error = Exception("Some exception")

            beforeEachGroup {
                whenever(localDataStore.addMovies(anyList()))
                    .thenReturn(Completable.error(error))
            }

            it("should emit same error") {
                val testObserver = movieRepository.addMovies(emptyList()).test()
                testObserver.assertError(error)
            }
        }
    }

    describe("#${MovieRepository::getMovieDetail.name}") {

        /*As of now, movieDetail is fetched from remote only,
        * we will do TDD later to fetch movieDetail from local
        */

        context("remote returns data") {

            val dummyData = dummyMovieDetail

            beforeEachGroup {
                whenever(remoteDataStore.getMovieDetail(anyString()))
                    .thenReturn(Single.just(dummyData))
            }

            it("should return the same data") {
                val testObserver = movieRepository.getMovieDetail("").test()
                testObserver.assertValue(dummyData)
            }
        }

        context("remote returns error") {

            val error = Exception("Some error")

            beforeEachGroup {
                whenever(remoteDataStore.getMovieDetail(anyString()))
                    .thenReturn(Single.error(error))
            }

            it("should return the same data") {
                val testObserver = movieRepository.getMovieDetail("").test()
                testObserver.assertError(error)
            }
        }
    }
})