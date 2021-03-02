package dev.rivu.mvijetpackcomposedemo.moviesearch.presentation

import dev.rivu.mvijetpackcomposedemo.base.MviIntent

sealed class MovieIntent : MviIntent {
    object InitialIntent : MovieIntent()
    data class SearchIntent(val query: String) : MovieIntent()
    data class ClickIntent(val imdbId: String) : MovieIntent()
    object ClearClickIntent : MovieIntent()
    data class SaveSearchHistory(val searchHistory: List<String>) : MovieIntent()
}