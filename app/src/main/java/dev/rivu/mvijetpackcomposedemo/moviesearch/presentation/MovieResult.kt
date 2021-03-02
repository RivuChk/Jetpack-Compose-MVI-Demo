package dev.rivu.mvijetpackcomposedemo.moviesearch.presentation

import dev.rivu.mvijetpackcomposedemo.base.MviResult
import dev.rivu.mvijetpackcomposedemo.moviesearch.data.model.Movie
import dev.rivu.mvijetpackcomposedemo.moviesearch.data.model.MovieDetail
import java.lang.Exception

sealed class MovieResult : MviResult {
    sealed class SearchResult : MovieResult() {
        abstract val query: String

        data class Success(val movies: List<Movie>, override val query: String) : SearchResult()
        data class Failure(val error: Throwable, override val query: String) : SearchResult()
        data class InProgress(override val query: String) : SearchResult()
    }

    sealed class LoadDetailResult : MovieResult() {
        abstract val imdbId: String

        data class Success(val movieDetail: MovieDetail, override val imdbId: String) :
            LoadDetailResult()

        data class Failure(val error: Throwable, override val imdbId: String) :
            LoadDetailResult()

        data class InProgress(override val imdbId: String) : LoadDetailResult()
    }

    object ClearDetailResult : MovieResult()

    data class InitResult(val searchHistory: List<String> = emptyList()) : MovieResult()

    sealed class SaveSearchResult : MovieResult() {
        object Loading : SaveSearchResult()
        object Success : SaveSearchResult()
        object Error : SaveSearchResult()
    }
}