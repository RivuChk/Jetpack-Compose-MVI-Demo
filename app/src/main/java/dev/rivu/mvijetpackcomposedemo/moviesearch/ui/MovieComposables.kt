package dev.rivu.mvijetpackcomposedemo.moviesearch.ui

import android.content.Context
import androidx.compose.Composable
import androidx.compose.MutableState
import androidx.compose.getValue
import androidx.compose.state
import androidx.lifecycle.LiveData
import androidx.ui.core.Alignment
import androidx.ui.core.Modifier
import androidx.ui.foundation.AdapterList
import androidx.ui.foundation.Clickable
import androidx.ui.foundation.Image
import androidx.ui.foundation.Text
import androidx.ui.graphics.imageFromResource
import androidx.ui.layout.Arrangement
import androidx.ui.layout.Column
import androidx.ui.livedata.observeAsState
import androidx.ui.material.CircularProgressIndicator
import androidx.ui.material.TopAppBar
import dev.rivu.mvijetpackcomposedemo.R
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

    val title = state { "Search Movies" }

    Column {
        TopAppBar(Modifier) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalGravity = Alignment.CenterHorizontally
            ) {
                Text(title.value)
            }
            Column(verticalArrangement = Arrangement.Center, horizontalGravity = Alignment.End) {
                Clickable(onClick = {
                    onSearch("Jack")
                }) {
                    Image(
                        imageFromResource(context.resources, R.drawable.ic_search)
                    )
                }
            }
        }

        when {
            moviesState.isIdleState() -> IdleScreen()
            moviesState.isLoading() -> LoadingScreen()
            moviesState.isDetailState() -> {
                val movieDetail = moviesState.detail!!
                DetailScreen(movieDetail)
                title.value = movieDetail.title
            }
            moviesState.movies.isNotEmpty() -> {
                title.value = moviesState.query
                ListScreen(moviesState.movies, onMovieClick)
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
        Clickable(onClick = {
            onMovieClick(movie.imdbID)
        }) {
            Text("Movie: ${movie.title}")
        }
    }
}

@Composable
fun ErrorScreen(throwable: Throwable) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalGravity = Alignment.CenterHorizontally
    ) {
        Text("Detail ${throwable.localizedMessage}")
    }
}

@Composable
fun LoadingScreen() {
    CircularProgressIndicator()
}

sealed class SearchState {
    object Icon : SearchState()
    data class Typing(val typedText: String) : SearchState()
    data class SearchTapped(val typedText: String) : SearchState()
}