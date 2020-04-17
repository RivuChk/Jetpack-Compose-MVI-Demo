package dev.rivu.mvijetpackcomposedemo.moviesearch.presentation

sealed class MovieAction {
    data class SearchAction(val query: String) : MovieAction()

    data class DetailAction(val imdbId: String) : MovieAction()

    object ClearDetailAction : MovieAction()
}