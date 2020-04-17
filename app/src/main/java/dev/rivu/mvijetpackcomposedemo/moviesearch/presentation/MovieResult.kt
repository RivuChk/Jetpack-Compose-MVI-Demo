package dev.rivu.mvijetpackcomposedemo.moviesearch.presentation

import dev.rivu.mvijetpackcomposedemo.moviesearch.data.model.Movie

sealed class MovieResult {
    sealed class SearchResult : MovieResult() {
        abstract val query: String

        data class Success(val movies: List<Movie>, override val query: String) : SearchResult()
        data class Failure(val errorMessage: String, override val query: String) : SearchResult()
        data class InProgress(override val query: String) : SearchResult()
    }

    sealed class LoadDetailResult : MovieResult() {
        abstract val imdbId: String

        data class Success(val movies: List<Movie>, override val imdbId: String) :
            LoadDetailResult()

        data class Failure(val errorMessage: String, override val imdbId: String) :
            LoadDetailResult()

        data class InProgress(override val imdbId: String) : LoadDetailResult()
    }

    object ClearDetailResult : MovieResult()
}