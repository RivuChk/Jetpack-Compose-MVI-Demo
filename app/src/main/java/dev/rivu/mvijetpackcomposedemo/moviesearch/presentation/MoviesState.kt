package dev.rivu.mvijetpackcomposedemo.moviesearch.presentation

import dev.rivu.mvijetpackcomposedemo.base.MviState
import dev.rivu.mvijetpackcomposedemo.moviesearch.data.model.Movie
import dev.rivu.mvijetpackcomposedemo.moviesearch.data.model.MovieDetail
import dev.rivu.mvijetpackcomposedemo.utils.emptyString

data class MoviesState(
    val query: String = emptyString(),
    val movies: List<Movie> = listOf(),
    val error: Throwable? = null,
    val isLoading: Boolean = false,
    val detail: MovieDetail? = null,
    val searchHistory: List<String> = emptyList()
) : MviState {
    companion object {
        fun initialState(): MoviesState = MoviesState()
    }
}

fun MoviesState.isIdleState() =
    query.isBlank() && movies.isEmpty() && error == null && !isLoading && detail == null

fun MoviesState.isDetailState() =
    detail != null

fun MoviesState.isLoading() = isLoading

fun MoviesState.resetDetailState(): MoviesState = copy(detail = null)