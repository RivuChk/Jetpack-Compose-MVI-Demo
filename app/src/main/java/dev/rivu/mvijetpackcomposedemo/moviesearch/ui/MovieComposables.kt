package dev.rivu.mvijetpackcomposedemo.moviesearch.ui

import android.content.Context
import androidx.compose.Composable
import androidx.compose.getValue
import androidx.compose.setValue
import androidx.compose.state
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.ui.core.Alignment
import androidx.ui.core.Modifier
import androidx.ui.foundation.AdapterList
import androidx.ui.foundation.Image
import androidx.ui.foundation.Text
import androidx.ui.foundation.clickable
import androidx.ui.graphics.imageFromResource
import androidx.ui.layout.Arrangement
import androidx.ui.layout.Column
import androidx.ui.livedata.observeAsState
import androidx.ui.material.CircularProgressIndicator
import androidx.ui.material.TopAppBar
import androidx.ui.tooling.preview.Preview
import dev.rivu.mvijetpackcomposedemo.R
import dev.rivu.mvijetpackcomposedemo.SEARCH_HINT
import dev.rivu.mvijetpackcomposedemo.moviesearch.data.model.Movie
import dev.rivu.mvijetpackcomposedemo.moviesearch.data.model.MovieDetail
import dev.rivu.mvijetpackcomposedemo.moviesearch.presentation.MoviesState
import dev.rivu.mvijetpackcomposedemo.moviesearch.presentation.isDetailState
import dev.rivu.mvijetpackcomposedemo.moviesearch.presentation.isIdleState
import dev.rivu.mvijetpackcomposedemo.moviesearch.presentation.isLoading

@Composable
fun MoviesScreen(
    context: Context,
    stateLiveData: LiveData<MoviesState>,
    onSearch: (String) -> Unit,
    onMovieClick: (String) -> Unit
) {
    val moviesState: MoviesState by stateLiveData.observeAsState(MoviesState.initialState())

    Column {
        val movieDetail = moviesState.detail
        //Appbar
        when {
            moviesState.isDetailState() && movieDetail != null -> {
                Appbar(context = context, searchState = SearchState.Detail(movieDetail.title), onSearch = onSearch)
            }
            !moviesState.query.isNullOrBlank() -> {
                Appbar(context = context, searchState = SearchState.SearchTyped(moviesState.query), onSearch = onSearch)
            }
            else -> {
                Appbar(context = context, searchState = SearchState.Icon, onSearch = onSearch)
            }
        }

        //Content
        when {
            moviesState.isIdleState() -> {
                IdleScreen()
            }
            moviesState.isLoading() -> {
                LoadingScreen()
            }
            moviesState.isDetailState() && movieDetail != null -> {
                DetailScreen(movieDetail)
            }
            moviesState.movies.isNotEmpty() -> {
                ListScreen(moviesState.movies, onMovieClick)
            }
            moviesState.error != null -> {
                ErrorScreen(moviesState.error!!)
            }
        }
    }
}

@Composable
fun Appbar(context: Context, searchState: SearchState, onSearch: (String) -> Unit) {
    TopAppBar {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalGravity = Alignment.CenterHorizontally
        ) {
            Text(searchState.titlebarText)
        }
        if (searchState !is SearchState.Detail) { //hide search bar in detail screen
            Column(verticalArrangement = Arrangement.Center, horizontalGravity = Alignment.End) {
                Image(
                    imageFromResource(context.resources, R.drawable.ic_search),
                    modifier = Modifier.clickable(onClick = {
                        onSearch("Jack")
                    })
                )
            }
        }
    }
}

@Composable
fun IdleScreen() {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalGravity = Alignment.CenterHorizontally
    ) {
        Text("Tap on Search button to Search for Movies")
    }
}

@Composable
fun DetailScreen(movieDetail: MovieDetail) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalGravity = Alignment.CenterHorizontally
    ) {
        Text("Detail $movieDetail")
    }
}

@Composable
fun ListScreen(movieList: List<Movie>, onMovieClick: (String) -> Unit) {
    AdapterList(movieList) { movie ->
        Text(
            "Movie: ${movie.title}",
            modifier = Modifier.clickable(onClick = {
                onMovieClick(movie.imdbID)
            })
        )
    }
}

@Composable
fun ErrorScreen(throwable: Throwable) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalGravity = Alignment.CenterHorizontally
    ) {
        Text("Error Showing Movies :: ${throwable.localizedMessage}")
    }
}

@Composable
@Preview
fun LoadingScreen() {
    CircularProgressIndicator()
}

@Composable
@Preview
fun listPreview() {
    ListScreen(movieList = listOf(
        Movie(
            title = "dsdad"
        )), onMovieClick = {})
}

@Composable
@Preview
fun errorPreview() {
    ErrorScreen(Exception("Unknown"))
}

sealed class SearchState(val titlebarText: String) {
    object Icon : SearchState(SEARCH_HINT)
    data class Typing(val typedText: String) : SearchState(typedText + "...")
    data class SearchTyped(val typedText: String) : SearchState(typedText)
    data class Detail(val movieTitle: String) : SearchState(movieTitle)
}