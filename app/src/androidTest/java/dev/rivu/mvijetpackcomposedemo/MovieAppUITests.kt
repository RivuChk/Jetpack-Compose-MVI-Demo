package dev.rivu.mvijetpackcomposedemo

import androidx.lifecycle.MutableLiveData
import androidx.test.filters.MediumTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.ui.test.*
import androidx.ui.test.android.AndroidComposeTestRule
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import dev.rivu.mvijetpackcomposedemo.moviesearch.presentation.MoviesState
import dev.rivu.mvijetpackcomposedemo.moviesearch.ui.APPBAR_SEARCH_ICON_TAG
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers.anyString

@MediumTest
@RunWith(JUnit4::class)
class MovieAppUITests {
    @get:Rule
    val composeTestRule = AndroidComposeTestRule<MainActivity>(null, false)

    val statesLiveData: MutableLiveData<MoviesState> by lazy {
        MutableLiveData<MoviesState>()
    }
    lateinit var mockOnSearch: (String) -> Unit
    lateinit var mockOnMovieClick: (String) -> Unit

    @Before
    fun setUp() {
        mockOnSearch = mock {
            whenever(mock.invoke(anyString()))
                .thenReturn(Unit)
        }

        mockOnMovieClick = mock {
            whenever(mock.invoke(anyString()))
                .thenReturn(Unit)
        }

        // Using targetContext as the Context of the instrumentation code
        composeTestRule.launchMoviesApp(
            statesLiveData,
            mockOnSearch, mockOnMovieClick
        )
    }

    @Test
    fun app_launches() {
        findByTag("appbar").assertIsDisplayed()
    }

    @Test
    fun test_idle_state() {
        statesLiveData.postValue(MoviesState.initialState())
        findByTag(APPBAR_SEARCH_ICON_TAG).assertIsDisplayed()
    }

    @Test
    fun test_search_action() {
        val searchQuery = "jack"
        statesLiveData.postValue(MoviesState.initialState())
        val searchIcon = findByTag(APPBAR_SEARCH_ICON_TAG)

        searchIcon.doClick()
        findByTag("searchBar").doClearText(false)
        findByTag("searchBar").doSendText(searchQuery, false)

        findByTag("searchButton").doClick()

        verify(mockOnSearch).invoke(searchQuery)
    }

    @Ignore("fails due to idling issue: https://github.com/RivuChk/Jetpack-Compose-MVI-Demo/issues/1")
    @Test
    fun test_loading_state() {
        statesLiveData.postValue(MoviesState.initialState().copy(isLoading = true))
        findByTag("progressbar").assertIsHidden()
    }

    @Ignore("fails due to idling issue: https://github.com/RivuChk/Jetpack-Compose-MVI-Demo/issues/1")
    @Test
    fun test_list_state() {
        val initialState = MoviesState.initialState()
        val movieList = generateDummyMovieList()

        statesLiveData.postValue(initialState.copy(movies = movieList))

        //test first and last items are displayed
        val firstMovie = movieList.first()
        val lastMovie = movieList.last()

        findBySubstring(text = firstMovie.title, ignoreCase = true).assertIsDisplayed()
        findBySubstring(text = lastMovie.title, ignoreCase = true).assertIsDisplayed()
    }
}