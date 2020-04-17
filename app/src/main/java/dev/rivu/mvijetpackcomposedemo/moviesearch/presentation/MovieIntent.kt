package dev.rivu.mvijetpackcomposedemo.moviesearch.presentation

sealed class MovieIntent {
    object InitialIntent : MovieIntent()
    data class SearchIntent(val query: String) : MovieIntent()
    data class ClickIntent(val imdbId: String) : MovieIntent()
    object ClearClickIntent : MovieIntent()
}