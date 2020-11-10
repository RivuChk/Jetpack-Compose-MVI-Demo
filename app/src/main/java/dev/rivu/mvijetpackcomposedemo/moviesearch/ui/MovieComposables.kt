package dev.rivu.mvijetpackcomposedemo.moviesearch.ui

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.InteractionState
import androidx.compose.foundation.Text
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumnItems
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.imageFromResource
import androidx.compose.ui.platform.ContextAmbient
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData
import androidx.ui.tooling.preview.Preview
import dev.rivu.mvijetpackcomposedemo.R
import dev.rivu.mvijetpackcomposedemo.SEARCH_HINT
import dev.rivu.mvijetpackcomposedemo.moviesearch.data.model.Movie
import dev.rivu.mvijetpackcomposedemo.moviesearch.data.model.MovieDetail
import dev.rivu.mvijetpackcomposedemo.moviesearch.presentation.MoviesState
import dev.rivu.mvijetpackcomposedemo.moviesearch.presentation.isDetailState
import dev.rivu.mvijetpackcomposedemo.moviesearch.presentation.isIdleState
import dev.rivu.mvijetpackcomposedemo.moviesearch.presentation.isLoading
import timber.log.Timber

@OptIn(ExperimentalStdlibApi::class)
@Composable
fun MoviesScreen(
    stateLiveData: LiveData<MoviesState>,
    onSearch: (String) -> Unit,
    onMovieClick: (String) -> Unit
) {
    val moviesState: MoviesState by stateLiveData.observeAsState(MoviesState.initialState())
    val movieDetail = moviesState.detail
    val searchState = when {
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

    Column {

        //Appbar
        Appbar(searchState = searchState, isIdle = moviesState.isIdleState(), onSearch = onSearch)

        val movies = moviesState.movies

        Timber.d("State: $moviesState")

        //Content
        when {
            moviesState.isLoading() -> {
                LoadingScreen()
            }
            moviesState.isDetailState() && movieDetail != null -> {
                DetailScreen(movieDetail)
            }
            movies.isNotEmpty() -> {
                ListScreen(movies, onMovieClick)
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
fun Appbar(searchState: SearchState, isIdle: Boolean = false, onSearch: (String) -> Unit) {

    val isSearchbarVisible = state { false }

    TopAppBar(Modifier.testTag("appbar")) {
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {

            val title = createRef()
            val searchIcon = createRef()


            Text(text = searchState.titlebarText, modifier = Modifier.constrainAs(title) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            })

            Image(
                imageFromResource(ContextAmbient.current.resources, R.drawable.ic_search),
                modifier = Modifier.constrainAs(searchIcon) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end)
                }.testTag(APPBAR_SEARCH_ICON_TAG)
                    .clickable(onClick = {
                        isSearchbarVisible.value = true
                    })
            )
        }
    }
    if (isSearchbarVisible.value) {
        SearchScreen(hint = "Search Movies", onSearch = {
            onSearch(it)
            isSearchbarVisible.value = false
        })
    } else if (isIdle) {
        IdleScreen()
    }
}

@Composable
fun IdleScreen() {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Tap on Search button to Search for Movies")
    }
}

@Composable
fun DetailScreen(movieDetail: MovieDetail) {
    val loadPictureState = loadPicture(movieDetail.poster, R.drawable.cinema)
    ConstraintLayout {
        val logo = createRef()
        val itemTitle = createRef()
        val itemType = createRef()
        val itemYear = createRef()
        val itemImdb = createRef()
        val itemPlot = createRef()

        Image(
            loadPictureState.image,
            modifier = Modifier.constrainAs(logo) {
                start.linkTo(parent.start)
                top.linkTo(parent.top)
                end.linkTo(parent.end)
            }.fillMaxWidth()
                .heightIn(max = 500.dp)
        )
        Text(
            text = movieDetail.title,
            color = Color.Blue,
            style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold),
            modifier = Modifier.constrainAs(itemTitle) {
                start.linkTo(parent.start)
                top.linkTo(logo.bottom)
            }.padding(8.dp)
        )
        Text(
            text = "Type: ${movieDetail.type.capitalize()}",
            style = TextStyle.Default.copy(fontSize = 15.sp),
            modifier = Modifier.constrainAs(itemType) {
                start.linkTo(parent.start)
                top.linkTo(itemImdb.bottom)
            }.padding(5.dp)
        )
        Text(
            text = "Year: ${movieDetail.year}",
            style = TextStyle.Default.copy(fontSize = 15.sp),
            modifier = Modifier.constrainAs(itemYear) {
                start.linkTo(parent.start)
                top.linkTo(itemType.bottom)
            }.padding(5.dp)
        )
        Text(
            text = "IMDB: ${movieDetail.imdbID}",
            style = TextStyle.Default.copy(fontSize = 15.sp),
            modifier = Modifier.constrainAs(itemImdb) {
                start.linkTo(parent.start)
                top.linkTo(itemPlot.bottom)
            }.padding(5.dp)
        )
        Text(
            text = "Plot: ${movieDetail.plot}",
            style = TextStyle.Default.copy(fontSize = 15.sp),
            modifier = Modifier.constrainAs(itemPlot) {
                start.linkTo(parent.start)
                top.linkTo(itemTitle.bottom)
            }.padding(5.dp)
        )
    }
}

@Composable
fun ListScreen(movieList: List<Movie>, onMovieClick: (String) -> Unit) {
    LazyColumnItems(movieList, modifier = Modifier.testTag("movieList")) { movie ->
        Box(modifier = Modifier.padding(5.dp).fillMaxWidth().heightIn(150.dp).clickable(onClick = {
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
        modifier = modifier.wrapContentHeight(align = Alignment.CenterVertically).fillMaxWidth()
            .drawShadow(2.dp)
    ) {
        val loadPictureState = loadPicture(movie.poster, R.drawable.cinema)
        ConstraintLayout {
            val logo = createRef()
            val itemTitle = createRef()
            val itemType = createRef()
            val itemYear = createRef()
            val itemImdb = createRef()


            Image(
                loadPictureState.image,
                modifier = Modifier.width(100.dp).height(100.dp).constrainAs(logo) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                }
            )
            Text(
                text = movie.title,
                color = Color.Blue,
                style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(5.dp).constrainAs(itemTitle) {
                    start.linkTo(parent.start)
                    top.linkTo(logo.bottom)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end)
                }
            )
            Text(
                text = "Type: ${movie.type.capitalize()}",
                style = TextStyle.Default.copy(fontSize = 10.sp),
                modifier = Modifier.padding(2.dp).constrainAs(itemType) {
                    start.linkTo(logo.end)
                    top.linkTo(itemImdb.bottom)
                    bottom.linkTo(itemYear.top)
                }
            )
            Text(
                text = "Year: ${movie.year}",
                style = TextStyle.Default.copy(fontSize = 10.sp),
                modifier = Modifier.padding(2.dp).constrainAs(itemYear) {
                    start.linkTo(logo.end)
                    top.linkTo(itemType.bottom)
                    bottom.linkTo(itemTitle.top)
                }
            )
            Text(
                text = "IMDB: ${movie.imdbID}",
                style = TextStyle.Default.copy(fontSize = 10.sp),
                modifier = Modifier.padding(2.dp).constrainAs(itemImdb) {
                    start.linkTo(logo.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(itemType.top)
                }
            )
        }
    }
}

@Composable
fun ErrorScreen(throwable: Throwable) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
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
    val typedText = state { TextFieldValue("") }
    Column {
        Row(modifier = Modifier.padding(5.dp)) {
            Text(text = "Enter Movie Name to Search")
        }
        Row {
            TextField(
                value = typedText.value,
                placeholder = @Composable {
                    Text(text = hint)
                },
                modifier = Modifier.fillMaxWidth().testTag("searchBar"),
                imeAction = ImeAction.Search,
                keyboardType = KeyboardType.Text,
                onImeActionPerformed = { action, keyboardController ->
                    if (action == ImeAction.Search) {
                        keyboardController?.hideSoftwareKeyboard()
                        onSearch(typedText.value.text)
                    }
                },
                onValueChange = { newTextValue ->
                    typedText.value = newTextValue
                },
                onTextInputStarted = { keyboardController ->
                    keyboardController.showSoftwareKeyboard()
                }
            )
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
    Appbar(searchState = SearchState.Icon, onSearch = {})
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