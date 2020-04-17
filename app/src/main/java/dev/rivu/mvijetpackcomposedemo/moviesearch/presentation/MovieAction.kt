package dev.rivu.mvijetpackcomposedemo.moviesearch.presentation

import dev.rivu.mvijetpackcomposedemo.base.MviAction

sealed class MovieAction : MviAction {
    object InitAction : MovieAction()

    data class SearchAction(val query: String) : MovieAction()

    data class DetailAction(val imdbId: String) : MovieAction()

    object ClearDetailAction : MovieAction()
}