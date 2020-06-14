package dev.rivu.mvijetpackcomposedemo.moviesearch.ui

import android.content.Context
import android.util.Log
import androidx.compose.*
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.ui.core.Alignment
import androidx.ui.core.Layout
import androidx.ui.core.Modifier
import androidx.ui.foundation.*
import androidx.ui.graphics.imageFromResource
import androidx.ui.input.ImeAction
import androidx.ui.input.KeyboardType
import androidx.ui.input.VisualTransformation
import androidx.ui.layout.*
import androidx.ui.layout.ColumnScope.weight
import androidx.ui.livedata.observeAsState
import androidx.ui.material.Button
import androidx.ui.material.CircularProgressIndicator
import androidx.ui.material.TopAppBar
import androidx.ui.text.TextLayoutResult
import androidx.ui.text.TextStyle
import androidx.ui.tooling.preview.Preview
import androidx.ui.unit.dp
import androidx.ui.unit.ipx
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
    val movieDetail = moviesState.detail
    val searchState = state {
        when {
            moviesState.isDetailState() && movieDetail != null -> {
                SearchState.Detail(movieDetail.title)
            }
            !moviesState.query.isNullOrBlank() -> {
                SearchState.SearchTyped(moviesState.query)
            }
            else -> {
                SearchState.Icon
            }
        }
    }

    Column {

        //Appbar
        Appbar(context = context, searchState = searchState.value) {
            searchState.value = SearchState.Typing("")
        }

        //Content
        when {
            searchState.value is SearchState.Typing -> {
                SearchScreen(hint = "Search Movies", onSearch = {
                    searchState.value = SearchState.SearchTyped(it)
                    onSearch(it)
                })
            }
            moviesState.isIdleState() -> {
                IdleScreen()
            }
            moviesState.isLoading() -> {
                LoadingScreen()
            }
            moviesState.isDetailState() && movieDetail != null -> {
                DetailScreen(movieDetail)
                searchState.value = SearchState.Detail(movieDetail.title)
            }
            moviesState.movies.isNotEmpty() -> {
                ListScreen(moviesState.movies, onMovieClick)
                searchState.value = SearchState.SearchTyped(moviesState.query)
            }
            moviesState.error != null -> {
                ErrorScreen(moviesState.error!!)
            }
        }
    }
}

@Composable
fun Appbar(context: Context, searchState: SearchState, onSearchTapped: () -> Unit) {
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
                    modifier = Modifier.clickable(onClick = onSearchTapped)
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
fun SearchScreen(hint: String, onSearch: (String) -> Unit) {
    val typedText = state { TextFieldValue(hint) }
    Column {
        Row {
            Text(text = "Enter Movie Name to Search")
        }
        Row {
            TextField(
                value = typedText.value,
                modifier = Modifier.fillMaxWidth(),
                onValueChange = {
                    typedText.value = it
                },
                onFocusChange = {
                    if (it && typedText.value.text.equals(hint, ignoreCase = false)) {
                        typedText.value = TextFieldValue("")
                    }
                })
        }
        Row(Modifier.fillMaxWidth().padding(5.dp)) {
            Column {
                Button(modifier = Modifier.padding(5.dp), onClick = {
                    onSearch(typedText.value.text)
                }) {
                    Text(text = "Search")
                }
            }
            Column {
                Button(modifier = Modifier.padding(5.dp), onClick = {
                    typedText.value = TextFieldValue("")
                }) {
                    Text(text = "Clear")
                }
            }
        }
    }
}

@Composable
@Preview
fun listPreview() {
    ListScreen(movieList = listOf(
        Movie(
            title = "dsdad"
        )
    ), onMovieClick = {})
}

@Composable
@Preview
fun searchPreview() {
    SearchScreen("hint") {
        Log.d("searched", it)
    }
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