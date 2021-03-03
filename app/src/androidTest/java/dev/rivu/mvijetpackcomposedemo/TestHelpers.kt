package dev.rivu.mvijetpackcomposedemo

import dev.rivu.mvijetpackcomposedemo.moviesearch.data.model.Movie
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