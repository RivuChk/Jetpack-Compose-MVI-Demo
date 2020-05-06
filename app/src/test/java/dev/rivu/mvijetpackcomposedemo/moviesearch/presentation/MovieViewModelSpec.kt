import androidx.arch.core.executor.ArchTaskExecutor
import androidx.arch.core.executor.TaskExecutor
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import dev.rivu.mvijetpackcomposedemo.moviesearch.data.IMovieRepository
import dev.rivu.mvijetpackcomposedemo.moviesearch.data.datafactory.dummyMovieDetail
import dev.rivu.mvijetpackcomposedemo.moviesearch.data.datafactory.dummyMovieSearchList
import dev.rivu.mvijetpackcomposedemo.moviesearch.presentation.*
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import org.mockito.ArgumentMatchers.anyString
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe


class MovieViewModelSpec : Spek({

    val mockRepository: IMovieRepository = mock()
    val fakeSchedulerProvider = FakeSchedulerProvider()
    lateinit var mockStateObserver: Observer<MoviesState>
    lateinit var viewModel: MovieViewModel

    beforeEachGroup {
        // In order to test LiveData, the `InstantTaskExecutorRule` rule needs to be applied via JUnit.
        // As we are running it with Spek, the "rule" will be implemented in this way instead
        ArchTaskExecutor.getInstance().setDelegate(object : TaskExecutor() {
            override fun executeOnDiskIO(runnable: Runnable) {
                runnable.run()
            }

            override fun isMainThread(): Boolean {
                return true
            }

            override fun postToMainThread(runnable: Runnable) {
                runnable.run()
            }
        })


        viewModel = MovieViewModel(
            MovieProcessor(
                mockRepository, fakeSchedulerProvider
            )
        )
        mockStateObserver = mock()
        viewModel.states().observeForever(mockStateObserver)
    }


    afterEachGroup {
        ArchTaskExecutor.getInstance().setDelegate(null)
    }

    describe("Test initial intent") {
        it("Should emit Idle State") {
            viewModel.processIntents(Observable.just(MovieIntent.InitialIntent))
            verify(mockStateObserver).onChanged(MoviesState.initialState())
        }
    }

    describe("Test search intent") {
        context("Repository emits Data") {

            val dummyData = dummyMovieSearchList

            beforeEachGroup {
                whenever(mockRepository.getMovies(anyString()))
                    .thenReturn(Flowable.just(dummyData))
            }

            it("Should reflect success state") {
                viewModel.processIntents(Observable.just(MovieIntent.SearchIntent("moviename")))

                val initialState = viewModel.states().value ?: MoviesState.initialState()


                verify(mockStateObserver).onChanged(
                    initialState
                        .copy(
                            isLoading = false,
                            query = "moviename",
                            movies = dummyData
                        )
                )
            }
        }
        context("Repository emits error") {

            val error = Exception("Some error")

            beforeEachTest {
                whenever(mockRepository.getMovies(anyString()))
                    .thenReturn(Flowable.error(error))
            }

            it("Should reflect error state") {
                viewModel.processIntents(Observable.just(MovieIntent.SearchIntent("moviename")))

                val initialState = viewModel.states().value ?: MoviesState.initialState()

                verify(mockStateObserver).onChanged(
                    initialState
                        .copy(
                            isLoading = false,
                            query = "moviename",
                            error = error
                        )
                )
            }
        }
    }

    describe("Test click intent") {
        context("Repository emits Data") {

            val dummyData = dummyMovieDetail

            beforeEachGroup {
                whenever(mockRepository.getMovieDetail(anyString()))
                    .thenReturn(Flowable.just(dummyData))
            }

            it("Should reflect success state") {
                viewModel.processIntents(Observable.just(MovieIntent.ClickIntent("id")))

                val initialState = viewModel.states().value ?: MoviesState.initialState()


                verify(mockStateObserver).onChanged(
                    initialState
                        .copy(
                            isLoading = false,
                            detail = dummyData
                        )
                )
            }
        }
        context("Repository emits error") {

            val error = Exception("Some error")

            beforeEachTest {
                whenever(mockRepository.getMovieDetail(anyString()))
                    .thenReturn(Flowable.error(error))
            }

            it("Should reflect error state") {
                viewModel.processIntents(Observable.just(MovieIntent.ClickIntent("id")))

                val initialState = viewModel.states().value ?: MoviesState.initialState()

                verify(mockStateObserver).onChanged(
                    initialState
                        .copy(
                            isLoading = false,
                            error = error
                        )
                )
            }
        }
    }
})