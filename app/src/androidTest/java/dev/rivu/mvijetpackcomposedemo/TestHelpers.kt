package dev.rivu.mvijetpackcomposedemo

import android.content.Context
import androidx.compose.material.MaterialTheme
import androidx.lifecycle.LiveData
import androidx.ui.test.ComposeTestRule
import dev.rivu.mvijetpackcomposedemo.moviesearch.data.model.Movie
import dev.rivu.mvijetpackcomposedemo.moviesearch.presentation.MoviesState
import dev.rivu.mvijetpackcomposedemo.moviesearch.ui.MoviesScreen
import kotlin.random.Random

/**
 * Launches the app from a test context
 */


fun generateDummyMovieList(): List<Movie> =
    (1..10).toList()
        .map {
            val random = Random(it).nextInt()
            Movie(
                imdbID = "id-$random-$it",
                title = "title-$random-$it"
            )
        }