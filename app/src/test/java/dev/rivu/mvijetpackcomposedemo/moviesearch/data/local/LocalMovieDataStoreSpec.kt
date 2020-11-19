import androidx.room.rxjava3.EmptyResultSetException
import com.nhaarman.mockitokotlin2.atLeastOnce
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import dev.rivu.mvijetpackcomposedemo.moviesearch.data.datafactory.dummyMovieDetail
import dev.rivu.mvijetpackcomposedemo.moviesearch.data.datafactory.dummyMovieEntityList
import dev.rivu.mvijetpackcomposedemo.moviesearch.data.datafactory.dummyMovieSearchList
import dev.rivu.mvijetpackcomposedemo.moviesearch.data.datafactory.getDummyMovieEntity
import dev.rivu.mvijetpackcomposedemo.moviesearch.data.local.LocalMovieDataStore
import dev.rivu.mvijetpackcomposedemo.moviesearch.data.local.database.MovieDao
import dev.rivu.mvijetpackcomposedemo.moviesearch.data.local.database.MovieEnitity
import dev.rivu.mvijetpackcomposedemo.moviesearch.data.model.Movie
import dev.rivu.mvijetpackcomposedemo.moviesearch.data.model.MovieDetail
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import org.mockito.ArgumentMatchers.anyList
import org.mockito.ArgumentMatchers.anyString
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe


class LocalMovieDataStoreSpec : Spek({

    val mockDao: MovieDao = mock()
    val localDataStore: LocalMovieDataStore by memoized {
        LocalMovieDataStore(mockDao)
    }

    describe("#${LocalMovieDataStore::getMoviesStream.name}") {

        context("dao emits two items") {

            val dummyEntityList1 = dummyMovieEntityList
            val dummyEntityList2 = dummyMovieEntityList

            val dummyMovieList1 = dummyEntityList1
                .map {
                    Movie(
                        imdbID = it.imdbID,
                        poster = it.poster,
                        title = it.title,
                        type = it.type,
                        year = it.year
                    )
                }

            val dummyMovieList2 = dummyEntityList2
                .map {
                    Movie(
                        imdbID = it.imdbID,
                        poster = it.poster,
                        title = it.title,
                        type = it.type,
                        year = it.year
                    )
                }

            beforeEachGroup {
                whenever(mockDao.getMoviesStream(anyString()))
                    .thenReturn(Flowable.just(dummyEntityList1, dummyEntityList2))
            }

            it("should emit same data") {
                val testObserver = localDataStore.getMoviesStream("query").test()
                testObserver.assertValueCount(2)
                testObserver.assertValues(dummyMovieList1, dummyMovieList2)
                testObserver.assertNoErrors()
            }
        }

        context("dao emits error") {

            val error = Exception("Some error")

            beforeEachGroup {
                whenever(mockDao.getMoviesStream(anyString()))
                    .thenReturn(Flowable.error(error))
            }

            it("should emit same data") {
                val testObserver = localDataStore.getMoviesStream("query").test()
                testObserver.assertError(error)
            }
        }
    }

    describe("#${LocalMovieDataStore::getMovies.name}") {

        context("dao emits data") {

            val dummyEntityList = dummyMovieEntityList

            val dummyMovieList = dummyEntityList
                .map {
                    Movie(
                        imdbID = it.imdbID,
                        poster = it.poster,
                        title = it.title,
                        type = it.type,
                        year = it.year
                    )
                }

            beforeEachGroup {
                whenever(mockDao.getMovies(anyString()))
                    .thenReturn(Single.just(dummyEntityList))
            }

            it("should emit same data") {
                val testObserver = localDataStore.getMovies("query").test()
                testObserver.assertValue(dummyMovieList)
            }
        }

        context("dao emits error") {

            val error = Exception("Some error")

            beforeEachGroup {
                whenever(mockDao.getMovies(anyString()))
                    .thenReturn(Single.error(error))
            }

            it("should emit same error") {
                val testObserver = localDataStore.getMovies("query").test()
                testObserver.assertError(error)
            }
        }
    }

    describe("#${LocalMovieDataStore::addMovies.name}") {

        context("dao completes") {

            val dummyMovieList = dummyMovieSearchList
            val dummyMovieEntityList = dummyMovieList.map {
                MovieEnitity(
                    imdbID = it.imdbID,
                    poster = it.poster,
                    title = it.title,
                    type = it.type,
                    year = it.year,
                    detail = null
                )
            }

            beforeEachGroup {
                whenever(mockDao.addMovies(dummyMovieEntityList))
                    .thenReturn(Completable.complete())
            }

            it("should pass the same data to DAO and complete") {
                val testObserver = localDataStore.addMovies(dummyMovieList).test()
                testObserver.assertComplete()
                verify(mockDao, atLeastOnce()).addMovies(dummyMovieEntityList)
            }
        }

        context("dao emits error") {

            val dummyMovieList = dummyMovieSearchList
            val dummyMovieEntityList = dummyMovieList.map {
                MovieEnitity(
                    imdbID = it.imdbID,
                    poster = it.poster,
                    title = it.title,
                    type = it.type,
                    year = it.year,
                    detail = null
                )
            }

            val error = Exception("Some error")

            beforeEachGroup {
                whenever(mockDao.addMovies(dummyMovieEntityList))
                    .thenReturn(Completable.error(error))
            }

            it("should emit same error") {
                val testObserver = localDataStore.addMovies(dummyMovieList).test()
                testObserver.assertError(error)
                verify(mockDao, atLeastOnce()).addMovies(dummyMovieEntityList)
            }
        }
    }

    describe("#${LocalMovieDataStore::getMovieDetail.name}") {

        context("dao emits data with null detail") {

            val dummyEntity = getDummyMovieEntity(1)
                .copy(
                    detail = null
                )

            beforeEachGroup {
                whenever(mockDao.getMovie(anyString()))
                    .thenReturn(Single.just(dummyEntity))
            }

            it("should emit error") {
                val testObserver = localDataStore.getMovieDetail("id").test()
                testObserver.assertError(EmptyResultSetException::class.java)
            }
        }

        context("dao emits data with detail") {

            val dummyEntity = getDummyMovieEntity(1)
            val dummyMovieDetail = MovieDetail(
                imdbID = dummyEntity.imdbID,
                poster = dummyEntity.poster,
                title = dummyEntity.title,
                type = dummyEntity.type,
                year = dummyEntity.year,

                response = dummyEntity.detail?.response ?: "",
                actors = dummyEntity.detail?.actors ?: emptyList(),
                awards = dummyEntity.detail?.awards ?: "",
                boxOffice = dummyEntity.detail?.boxOffice ?: "",
                country = dummyEntity.detail?.country ?: "",
                dVD = dummyEntity.detail?.dVD ?: "",
                director = dummyEntity.detail?.director ?: "",
                genre = dummyEntity.detail?.genre ?: "",
                imdbRating = dummyEntity.detail?.imdbRating ?: "",
                imdbVotes = dummyEntity.detail?.imdbVotes ?: "",
                language = dummyEntity.detail?.language ?: "",
                metascore = dummyEntity.detail?.metascore ?: "",
                plot = dummyEntity.detail?.plot ?: "",
                production = dummyEntity.detail?.production ?: "",
                rated = dummyEntity.detail?.rated ?: "",
                ratings = dummyEntity.detail?.ratings?.map {
                    MovieDetail.Rating(
                        source = it.source,
                        value = it.value
                    )
                } ?: emptyList(),
                released = dummyEntity.detail?.released ?: "",
                runtime = dummyEntity.detail?.runtime ?: "",
                website = dummyEntity.detail?.website ?: "",
                writer = dummyEntity.detail?.writer ?: ""
            )

            beforeEachGroup {
                whenever(mockDao.getMovie(anyString()))
                    .thenReturn(Single.just(dummyEntity))
            }

            it("should emit same data") {
                val testObserver = localDataStore.getMovieDetail("id").test()
                testObserver.assertValue(dummyMovieDetail)
            }
        }

        context("dao emits error") {

            val error = Exception("Some error")

            beforeEachGroup {
                whenever(mockDao.getMovie(anyString()))
                    .thenReturn(Single.error(error))
            }

            it("should emit same error") {
                val testObserver = localDataStore.getMovieDetail("id").test()
                testObserver.assertError(error)
            }
        }
    }

    describe("#${LocalMovieDataStore::addMovieDetail.name}") {

        context("dao completes") {

            val dummyMovieDetail = dummyMovieDetail
            val dummyMovieEntity = MovieEnitity(
                imdbID = dummyMovieDetail.imdbID,
                poster = dummyMovieDetail.poster,
                title = dummyMovieDetail.title,
                type = dummyMovieDetail.type,
                year = dummyMovieDetail.year,
                detail = MovieEnitity.Detail(
                    response = dummyMovieDetail.response,
                    actors = dummyMovieDetail.actors,
                    awards = dummyMovieDetail.awards,
                    boxOffice = dummyMovieDetail.boxOffice,
                    country = dummyMovieDetail.country,
                    dVD = dummyMovieDetail.dVD,
                    director = dummyMovieDetail.director,
                    genre = dummyMovieDetail.genre,
                    imdbRating = dummyMovieDetail.imdbRating,
                    imdbVotes = dummyMovieDetail.imdbVotes,
                    language = dummyMovieDetail.language,
                    metascore = dummyMovieDetail.metascore,
                    plot = dummyMovieDetail.plot,
                    production = dummyMovieDetail.production,
                    rated = dummyMovieDetail.rated,
                    ratings = dummyMovieDetail.ratings.map {
                        MovieEnitity.Detail.Rating(
                            source = it.source,
                            value = it.value
                        )
                    },
                    released = dummyMovieDetail.released,
                    runtime = dummyMovieDetail.runtime,
                    website = dummyMovieDetail.website,
                    writer = dummyMovieDetail.writer
                )
            )

            beforeEachGroup {
                whenever(mockDao.updateMovieInDB(dummyMovieEntity))
                    .thenReturn(Completable.complete())
            }

            it("should pass the same data to DAO and complete") {
                val testObserver = localDataStore.addMovieDetail(dummyMovieDetail).test()
                testObserver.assertComplete()
                verify(mockDao, atLeastOnce()).updateMovieInDB(dummyMovieEntity)
            }
        }

        context("dao emits error") {

            val dummyMovieDetail = dummyMovieDetail
            val dummyMovieEntity = MovieEnitity(
                imdbID = dummyMovieDetail.imdbID,
                poster = dummyMovieDetail.poster,
                title = dummyMovieDetail.title,
                type = dummyMovieDetail.type,
                year = dummyMovieDetail.year,
                detail = MovieEnitity.Detail(
                    response = dummyMovieDetail.response,
                    actors = dummyMovieDetail.actors,
                    awards = dummyMovieDetail.awards,
                    boxOffice = dummyMovieDetail.boxOffice,
                    country = dummyMovieDetail.country,
                    dVD = dummyMovieDetail.dVD,
                    director = dummyMovieDetail.director,
                    genre = dummyMovieDetail.genre,
                    imdbRating = dummyMovieDetail.imdbRating,
                    imdbVotes = dummyMovieDetail.imdbVotes,
                    language = dummyMovieDetail.language,
                    metascore = dummyMovieDetail.metascore,
                    plot = dummyMovieDetail.plot,
                    production = dummyMovieDetail.production,
                    rated = dummyMovieDetail.rated,
                    ratings = dummyMovieDetail.ratings.map {
                        MovieEnitity.Detail.Rating(
                            source = it.source,
                            value = it.value
                        )
                    },
                    released = dummyMovieDetail.released,
                    runtime = dummyMovieDetail.runtime,
                    website = dummyMovieDetail.website,
                    writer = dummyMovieDetail.writer
                )
            )

            val error = Exception("Some error")

            beforeEachGroup {
                whenever(mockDao.updateMovieInDB(dummyMovieEntity))
                    .thenReturn(Completable.error(error))
            }

            it("should emit same error") {
                val testObserver = localDataStore.addMovieDetail(dummyMovieDetail).test()
                testObserver.assertError(error)
                verify(mockDao, atLeastOnce()).updateMovieInDB(dummyMovieEntity)
            }
        }
    }
})