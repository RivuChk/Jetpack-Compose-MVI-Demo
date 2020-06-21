package dev.rivu.mvijetpackcomposedemo

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.ui.material.MaterialTheme
import androidx.ui.material.Surface
import androidx.ui.test.ComposeTestRule
import dev.rivu.mvijetpackcomposedemo.moviesearch.presentation.MoviesState
import dev.rivu.mvijetpackcomposedemo.moviesearch.ui.MoviesScreen

/**
 * Launches the app from a test context
 */
fun ComposeTestRule.launchMoviesApp(
    context: Context,
    stateLiveData: LiveData<MoviesState>,
    onSearch: (String) -> Unit,
    onMovieClick: (String) -> Unit
) {
    setContent {
        MaterialTheme {
            Surface {
                MoviesScreen(
                    context = context,
                    stateLiveData = stateLiveData,
                    onSearch = onSearch,
                    onMovieClick = onMovieClick
                )
            }
        }

    }
}