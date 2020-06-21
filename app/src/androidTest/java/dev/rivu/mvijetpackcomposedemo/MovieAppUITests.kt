package dev.rivu.mvijetpackcomposedemo

import androidx.lifecycle.MutableLiveData
import androidx.test.filters.MediumTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.ui.test.android.AndroidComposeTestRule
import androidx.ui.test.assertIsDisplayed
import androidx.ui.test.findByTag
import com.nhaarman.mockitokotlin2.mock
import dev.rivu.mvijetpackcomposedemo.moviesearch.presentation.MoviesState
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

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
        mockOnSearch = mock()
        mockOnMovieClick = mock()

        // Using targetContext as the Context of the instrumentation code
        composeTestRule.launchMoviesApp(
            InstrumentationRegistry.getInstrumentation().targetContext,
            statesLiveData,
            mockOnSearch, mockOnMovieClick
        )
    }

    @Test
    fun app_launches() {
        findByTag("appbar").assertIsDisplayed()
    }
}