package dev.rivu.mvijetpackcomposedemo

import androidx.compose.material.MaterialTheme
import androidx.lifecycle.MutableLiveData
import androidx.test.filters.MediumTest
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import dev.rivu.mvijetpackcomposedemo.moviesearch.presentation.MoviesState
import dev.rivu.mvijetpackcomposedemo.moviesearch.ui.APPBAR_SEARCH_ICON_TAG
import dev.rivu.mvijetpackcomposedemo.moviesearch.ui.MoviesScreen
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers.anyString

@MediumTest
@RunWith(JUnit4::class)
class MovieAppUITests {
    @get:Rule
    val composeTestRule = createComposeRule()

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


        composeTestRule.setContent {
            MaterialTheme {
                MoviesScreen(
                    stateLiveData = statesLiveData,
                    onSearch = mockOnSearch,
                    onMovieClick = mockOnMovieClick
                )
            }
        }
    }

    @Test
    fun splashPlays() {
        composeTestRule.onNode(hasTestTag("splash")).assertIsDisplayed()
    }

    @Test
    fun test_idle_state() {
        statesLiveData.postValue(MoviesState.initialState().copy(skipSplash = true))
        composeTestRule.onNode(hasTestTag(APPBAR_SEARCH_ICON_TAG)).assertIsDisplayed()
    }

    @Test
    fun test_search_action() {
        val searchQuery = "jack"
        statesLiveData.postValue(MoviesState.initialState().copy(skipSplash = true))
        val searchIcon = composeTestRule.onNode(hasTestTag(APPBAR_SEARCH_ICON_TAG))

        searchIcon.performClick()
        composeTestRule.onNode(hasTestTag("searchBar")).performTextClearance()
        composeTestRule.onNode(hasTestTag("searchBar")).performTextInput(searchQuery)

        composeTestRule.onNode(hasTestTag("searchBar")).performImeAction()

        verify(mockOnSearch).invoke(searchQuery)
    }

    @Test
    fun test_loading_state() {
        statesLiveData.postValue(MoviesState.initialState().copy(isLoading = true, skipSplash = true))
        composeTestRule.onNode(hasTestTag("progressbar")).assertExists()
    }

    @Test
    fun test_list_state() {
        val initialState = MoviesState.initialState().copy(skipSplash = true)
        val movieList = generateDummyMovieList()

        statesLiveData.postValue(initialState.copy(movies = movieList))

        //test first and last items are displayed
        val firstMovie = movieList.first()

        composeTestRule.onNode(hasText(text = firstMovie.title, substring = true, ignoreCase = true)).assertIsDisplayed()
    }
}