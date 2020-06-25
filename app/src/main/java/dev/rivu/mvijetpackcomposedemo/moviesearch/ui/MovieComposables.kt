package dev.rivu.mvijetpackcomposedemo.moviesearch.ui

import android.util.Log
import androidx.compose.Composable
import androidx.compose.getValue
import androidx.compose.state
import androidx.lifecycle.LiveData
import androidx.ui.core.*
import androidx.ui.foundation.*
import androidx.ui.foundation.shape.corner.RoundedCornerShape
import androidx.ui.graphics.Color
import androidx.ui.graphics.imageFromResource
import androidx.ui.input.TextFieldValue
import androidx.ui.layout.*
import androidx.ui.livedata.observeAsState
import androidx.ui.material.Button
import androidx.ui.material.Card
import androidx.ui.material.CircularProgressIndicator
import androidx.ui.material.TopAppBar
import androidx.ui.text.TextStyle
import androidx.ui.text.font.FontWeight
import androidx.ui.tooling.preview.Preview
import androidx.ui.unit.dp
import androidx.ui.unit.sp
import dev.rivu.mvijetpackcomposedemo.R
import dev.rivu.mvijetpackcomposedemo.SEARCH_HINT
import dev.rivu.mvijetpackcomposedemo.moviesearch.data.model.Movie
import dev.rivu.mvijetpackcomposedemo.moviesearch.data.model.MovieDetail
import dev.rivu.mvijetpackcomposedemo.moviesearch.presentation.MoviesState
import dev.rivu.mvijetpackcomposedemo.moviesearch.presentation.isDetailState
import dev.rivu.mvijetpackcomposedemo.moviesearch.presentation.isIdleState
import dev.rivu.mvijetpackcomposedemo.moviesearch.presentation.isLoading

@OptIn(ExperimentalStdlibApi::class)
@Composable
fun MoviesScreen(
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
        Appbar(searchState = searchState.value) {
            searchState.value = SearchState.Typing()
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

const val ITEM_LOGO_TAG = "item_logo"
const val ITEM_TITLE_TAG = "item_title"
const val ITEM_IMDB_TAG = "item_imdb"
const val ITEM_YEAR_TAG = "item_year"
const val ITEM_TYPE_TAG = "item_type"
const val ITEM_PLOT_TAG = "item_plot"

const val APPBAR_TITLE_TAG = "appbar_title"
const val APPBAR_SEARCH_ICON_TAG = "searchIcon"


@Composable
fun Appbar(searchState: SearchState, onSearchTapped: () -> Unit) {
    TopAppBar(Modifier.testTag("appbar")) {
        val isSearchVisible = searchState !is SearchState.Detail
        ConstraintLayout(constraintSet = ConstraintSet {
            val title = tag(APPBAR_TITLE_TAG)
            val searchIcon = tag(APPBAR_SEARCH_ICON_TAG)

            title.apply {
                top constrainTo parent.top
                bottom constrainTo parent.bottom
                left constrainTo parent.left
                right constrainTo parent.right
            }

            searchIcon.apply {
                top constrainTo parent.top
                bottom constrainTo parent.bottom
                right constrainTo parent.right
            }
        }, modifier = Modifier.fillMaxSize()) {
            Text(text = searchState.titlebarText, modifier = Modifier.tag(APPBAR_TITLE_TAG))

            Image(
                imageFromResource(ContextAmbient.current.resources, R.drawable.ic_search),
                modifier = Modifier.tag(APPBAR_SEARCH_ICON_TAG).testTag(APPBAR_SEARCH_ICON_TAG)
                    .clickable(onClick = onSearchTapped)
            )
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
    val loadPictureState = loadPicture(movieDetail.poster, R.drawable.cinema)
    ConstraintLayout(constraintSet = ConstraintSet {
        val logo = tag(ITEM_LOGO_TAG)
        val itemTitle = tag(ITEM_TITLE_TAG)
        val itemType = tag(ITEM_TYPE_TAG)
        val itemYear = tag(ITEM_YEAR_TAG)
        val itemImdb = tag(ITEM_IMDB_TAG)
        val itemPlot = tag(ITEM_PLOT_TAG)

        logo.apply {
            left constrainTo parent.left
            top constrainTo parent.top
            right constrainTo parent.right
        }

        itemTitle.apply {
            left constrainTo parent.left
            top constrainTo logo.bottom
        }

        itemPlot.apply {
            left constrainTo parent.left
            top constrainTo itemTitle.bottom
        }

        itemImdb.apply {
            left constrainTo parent.left
            top constrainTo itemPlot.bottom
        }

        itemType.apply {
            left constrainTo parent.left
            top constrainTo itemImdb.bottom
        }

        itemYear.apply {
            left constrainTo parent.left
            top constrainTo itemType.bottom
        }
    }) {
        Image(
            loadPictureState.image,
            modifier = Modifier.tag(ITEM_LOGO_TAG).fillMaxWidth().heightIn(maxHeight = 500.dp)
        )
        Text(
            text = movieDetail.title,
            color = Color.Blue,
            style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold),
            modifier = Modifier.tag(ITEM_TITLE_TAG).padding(8.dp)
        )
        Text(text = "Type: ${movieDetail.type.capitalize()}", style = TextStyle.Default.copy(fontSize = 15.sp), modifier = Modifier.tag(ITEM_TYPE_TAG).padding(5.dp))
        Text(text = "Year: ${movieDetail.year}", style = TextStyle.Default.copy(fontSize = 15.sp), modifier = Modifier.tag(ITEM_YEAR_TAG).padding(5.dp))
        Text(text = "IMDB: ${movieDetail.imdbID}", style = TextStyle.Default.copy(fontSize = 15.sp), modifier = Modifier.tag(ITEM_IMDB_TAG).padding(5.dp))
        Text(text = "Plot: ${movieDetail.plot}", style = TextStyle.Default.copy(fontSize = 15.sp), modifier = Modifier.tag(ITEM_PLOT_TAG).padding(5.dp))
    }
}

@Composable
fun ListScreen(movieList: List<Movie>, onMovieClick: (String) -> Unit) {
    AdapterList(movieList) { movie ->
        Box(modifier = Modifier.padding(5.dp).fillMaxWidth().heightIn(maxHeight = 150.dp).clickable(onClick = {
            onMovieClick(movie.imdbID)
        })) {
            MovieItemCard(
                movie = movie
            )
        }
    }
}

@Composable
fun MovieItemCard(modifier: Modifier = Modifier, movie: Movie) {
    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = 7.dp,
        modifier = modifier.wrapContentHeight(align = Alignment.CenterVertically).fillMaxWidth().drawShadow(2.dp)
    ) {
        val loadPictureState = loadPicture(movie.poster, R.drawable.cinema)
        ConstraintLayout(constraintSet = ConstraintSet {
            val logo = tag(ITEM_LOGO_TAG)
            val itemTitle = tag(ITEM_TITLE_TAG)
            val itemType = tag(ITEM_TYPE_TAG)
            val itemYear = tag(ITEM_YEAR_TAG)
            val itemImdb = tag(ITEM_IMDB_TAG)

            logo.apply {
                left constrainTo parent.left
                top constrainTo parent.top
            }

            itemTitle.apply {
                left constrainTo parent.left
                top constrainTo logo.bottom
                bottom constrainTo parent.bottom
                right constrainTo parent.right
            }

            itemImdb.apply {
                left constrainTo logo.right
                top constrainTo parent.top
                bottom constrainTo itemType.top
            }

            itemType.apply {
                left constrainTo logo.right
                top constrainTo itemImdb.bottom
                bottom constrainTo itemYear.top
            }

            itemYear.apply {
                left constrainTo logo.right
                top constrainTo itemType.bottom
                bottom constrainTo itemTitle.top
            }
        }) {
            Image(
                loadPictureState.image,
                modifier = Modifier.width(100.dp).height(100.dp).tag(ITEM_LOGO_TAG)
            )
            Text(
                text = movie.title,
                color = Color.Blue,
                style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Bold),
                modifier = Modifier.tag(ITEM_TITLE_TAG).padding(5.dp)
            )
            Text(text = "Type: ${movie.type.capitalize()}", style = TextStyle.Default.copy(fontSize = 10.sp), modifier = Modifier.tag(ITEM_TYPE_TAG).padding(2.dp))
            Text(text = "Year: ${movie.year}", style = TextStyle.Default.copy(fontSize = 10.sp), modifier = Modifier.tag(ITEM_YEAR_TAG).padding(2.dp))
            Text(text = "IMDB: ${movie.imdbID}", style = TextStyle.Default.copy(fontSize = 10.sp), modifier = Modifier.tag(ITEM_IMDB_TAG).padding(2.dp))
        }
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
fun LoadingScreen() {
    CircularProgressIndicator(Modifier.testTag("progressbar").fillMaxSize())
}

@Composable
fun SearchScreen(hint: String, onSearch: (String) -> Unit) {
    val typedText = state { TextFieldValue(hint) }
    Column {
        Row(modifier = Modifier.padding(5.dp)) {
            Text(text = "Enter Movie Name to Search")
        }
        Row {
            TextField(
                value = typedText.value,
                modifier = Modifier.fillMaxWidth().testTag("searchBar"),
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
                    Text(text = "Search", modifier = Modifier.testTag("searchButton"))
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
fun loadingPreview() {
    LoadingScreen()
}

@Composable
@Preview
fun appbarPreview() {
    Appbar(searchState = SearchState.Icon, onSearchTapped = {})
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
    data class Typing(val typedText: String = "Typing...") : SearchState(typedText)
    data class SearchTyped(val typedText: String) : SearchState(typedText)
    data class Detail(val movieTitle: String) : SearchState(movieTitle)
}