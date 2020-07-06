package dev.rivu.mvijetpackcomposedemo

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.ui.material.MaterialTheme
import androidx.ui.material.Surface
import androidx.ui.test.ComposeTestRule
import dev.rivu.mvijetpackcomposedemo.moviesearch.data.model.Movie
import dev.rivu.mvijetpackcomposedemo.moviesearch.presentation.MoviesState
import dev.rivu.mvijetpackcomposedemo.moviesearch.ui.MoviesScreen
import kotlin.random.Random

/**
 * Launches the app from a test context
 */
fun ComposeTestRule.launchMoviesApp(
    stateLiveData: LiveData<MoviesState>,
    onSearch: (String) -> Unit,
    onMovieClick: (String) -> Unit
) {
    setContent {
        MaterialTheme {
            Surface {
                MoviesScreen(
                    stateLiveData = stateLiveData,
                    onSearch = onSearch,
                    onMovieClick = onMovieClick
                )
            }
        }

    }
}

fun generateDummyMovieList(): List<Movie> =
    (1..10).toList()
        .map {
            val random = Random(it).nextInt()
            Movie(
                imdbID = "id-$random-$it",
                title = "title-$random-$it"
            )
        }