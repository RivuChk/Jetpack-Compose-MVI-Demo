package dev.rivu.mvijetpackcomposedemo.moviesearch.ui

import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.LiveData
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import coil.transition.CrossfadeTransition
import com.airbnb.lottie.compose.*
import dev.rivu.mvijetpackcomposedemo.R
import dev.rivu.mvijetpackcomposedemo.SEARCH_HINT
import dev.rivu.mvijetpackcomposedemo.moviesearch.data.model.Movie
import dev.rivu.mvijetpackcomposedemo.moviesearch.data.model.MovieDetail
import dev.rivu.mvijetpackcomposedemo.moviesearch.presentation.MoviesState
import dev.rivu.mvijetpackcomposedemo.moviesearch.presentation.isDetailState
import dev.rivu.mvijetpackcomposedemo.moviesearch.presentation.isIdleState
import dev.rivu.mvijetpackcomposedemo.moviesearch.presentation.isLoading
import timber.log.Timber

@Composable
fun MoviesScreen(
    stateLiveData: LiveData<MoviesState>,
    onSearch: (String) -> Unit,
    onMovieClick: (String) -> Unit
) {

    val isSplashPlaying = remember { mutableStateOf(true) }

    val moviesState: MoviesState by stateLiveData.observeAsState(MoviesState.initialState())

    if (isSplashPlaying.value && !moviesState.skipSplash) {
        Splash(isSplashPlaying)
    } else {
        val movieDetail = moviesState.detail
        val searchState = when {
            moviesState.isDetailState() && movieDetail != null -> {
                SearchState.Detail(movieDetail.title)
            }
            !moviesState.query.isBlank() -> {
                SearchState.SearchTyped(moviesState.query)
            }
            else -> {
                SearchState.Icon
            }
        }

        Column {

            //Appbar
            Appbar(
                searchState = searchState,
                isIdle = moviesState.isIdleState(),
                onSearch = onSearch
            )

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
                else -> {
                    IdleScreen(searchHistory = moviesState.searchHistory, onSearch = onSearch)
                }
            }
        }
    }

}

const val APPBAR_SEARCH_ICON_TAG = "searchIcon"

@Composable
fun Splash(
    isPlaying: MutableState<Boolean> = remember { mutableStateOf(false) }
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.splash_animation_jetpack))
    val progress by animateLottieCompositionAsState(composition)

    ConstraintLayout(modifier = Modifier.fillMaxSize().semantics { testTag = "splash" }) {
        val lottie = createRef()
        val credit = createRef()


        LottieAnimation(
            composition,
            progress,
            modifier = Modifier
                .constrainAs(lottie) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
                .size(100.dp)
        )

        Text(
            text = "Lottie from: https://lottiefiles.com/29328-android-jetpack",
            fontSize = 8.sp,
            modifier = Modifier.constrainAs(credit) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                top.linkTo(lottie.bottom)
                bottom.linkTo(parent.bottom)
            })
    }

    isPlaying.value = progress < 1f
}

@Composable
fun Appbar(searchState: SearchState, isIdle: Boolean = false, onSearch: (String) -> Unit) {

    val isSearchbarVisible = remember { mutableStateOf(false) }

    TopAppBar(Modifier.semantics { testTag = "appbar" }) {
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
                painterResource(R.drawable.ic_search),
                contentDescription = "Search",
                modifier = Modifier
                    .constrainAs(searchIcon) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        end.linkTo(parent.end)
                    }
                    .clickable(onClick = {
                        isSearchbarVisible.value = true
                    })
                    .semantics { testTag = APPBAR_SEARCH_ICON_TAG }
            )
        }
    }
    if (isSearchbarVisible.value) {
        SearchScreen(hint = "Search Movies", onSearch = {
            onSearch(it)
            isSearchbarVisible.value = false
        })
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun IdleScreen(searchHistory: List<String>, onSearch: (String) -> Unit = {}) {
    Column {
        Row {
            Text("Tap on Search button to Search for Movies")
        }

        if (searchHistory.isNotEmpty()) {
            Row(Modifier.padding(top = 5.dp)) {
                Text("Below are your past searches")
            }

            LazyVerticalGrid(cells = GridCells.Fixed(3)) {
                items(searchHistory) { item ->
                    Column(modifier = Modifier.padding(5.dp)) {
                        Box(
                            modifier = Modifier
                                .border(
                                    width = 1.dp,
                                    color = Color.Gray,
                                    shape = RoundedCornerShape(5.dp)
                                )
                                .background(color = Color.Red, shape = RoundedCornerShape(5.dp))
                                .padding(5.dp)
                                .clickable {
                                    onSearch(item)
                                }
                        ) {
                            Text(
                                text = item,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun DetailScreen(movieDetail: MovieDetail) {
    ConstraintLayout {
        val logo = createRef()
        val itemTitle = createRef()
        val itemType = createRef()
        val itemYear = createRef()
        val itemImdb = createRef()
        val itemPlot = createRef()

        Image(
            painter = rememberImagePainter(
                data = movieDetail.poster,
                builder = {
                    transition(CrossfadeTransition())
                    placeholder(R.drawable.cinema)
                }
            ),
            contentDescription = movieDetail.title + " poster",
            modifier = Modifier
                .constrainAs(logo) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                }
                .fillMaxWidth()
                .heightIn(max = 500.dp)
        )

        Text(
            text = movieDetail.title,
            color = Color.Blue,
            style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold),
            modifier = Modifier
                .constrainAs(itemTitle) {
                    start.linkTo(parent.start)
                    top.linkTo(logo.bottom)
                }
                .padding(8.dp)
        )
        Text(
            text = "Type: ${movieDetail.type.capitalize()}",
            style = TextStyle.Default.copy(fontSize = 15.sp),
            modifier = Modifier
                .constrainAs(itemType) {
                    start.linkTo(parent.start)
                    top.linkTo(itemImdb.bottom)
                }
                .padding(5.dp)
        )
        Text(
            text = "Year: ${movieDetail.year}",
            style = TextStyle.Default.copy(fontSize = 15.sp),
            modifier = Modifier
                .constrainAs(itemYear) {
                    start.linkTo(parent.start)
                    top.linkTo(itemType.bottom)
                }
                .padding(5.dp)
        )
        Text(
            text = "IMDB: ${movieDetail.imdbID}",
            style = TextStyle.Default.copy(fontSize = 15.sp),
            modifier = Modifier
                .constrainAs(itemImdb) {
                    start.linkTo(parent.start)
                    top.linkTo(itemPlot.bottom)
                }
                .padding(5.dp)
        )
        Text(
            text = "Plot: ${movieDetail.plot}",
            style = TextStyle.Default.copy(fontSize = 15.sp),
            modifier = Modifier
                .constrainAs(itemPlot) {
                    start.linkTo(parent.start)
                    top.linkTo(itemTitle.bottom)
                }
                .padding(5.dp)
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ListScreen(movieList: List<Movie>, onMovieClick: (String) -> Unit) {
    LazyVerticalGrid(
        modifier = Modifier.semantics { testTag = "movieList" },
        cells = GridCells.Fixed(2)
    ) {
        items(movieList) { movie ->
            Box(
                modifier = Modifier
                    .padding(5.dp)
                    .fillMaxWidth()
                    .height(150.dp)
                    .clickable(onClick = {
                        onMovieClick(movie.imdbID)
                    })
            ) {
                MovieItemCard(
                    movie = movie
                )
            }
        }
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun MovieItemCard(modifier: Modifier = Modifier, movie: Movie) {
    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = 7.dp,
        modifier = modifier
            .wrapContentHeight(align = Alignment.CenterVertically)
            .fillMaxWidth()
            .shadow(2.dp)
    ) {
        ConstraintLayout {
            val logo = createRef()
            val itemTitle = createRef()
            val itemType = createRef()
            val itemYear = createRef()
            val itemImdb = createRef()


            Image(
                painter = rememberImagePainter(
                    data = movie.poster,
                    builder = {
                        transition(CrossfadeTransition())
                        placeholder(R.drawable.cinema)
                    }
                ),
                contentDescription = movie.title + " poster",
                modifier = Modifier
                    .width(100.dp)
                    .height(100.dp)
                    .constrainAs(logo) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                    }
            )

            Text(
                text = movie.title,
                color = Color.Blue,
                style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Bold),
                modifier = Modifier
                    .padding(5.dp)
                    .constrainAs(itemTitle) {
                        start.linkTo(parent.start)
                        top.linkTo(logo.bottom)
                        bottom.linkTo(parent.bottom)
                        end.linkTo(parent.end)
                    }
            )
            Text(
                text = "Type: ${movie.type.capitalize()}",
                style = TextStyle.Default.copy(fontSize = 10.sp),
                modifier = Modifier
                    .padding(2.dp)
                    .constrainAs(itemType) {
                        start.linkTo(logo.end)
                        top.linkTo(itemImdb.bottom)
                        bottom.linkTo(itemYear.top)
                    }
            )
            Text(
                text = "Year: ${movie.year}",
                style = TextStyle.Default.copy(fontSize = 10.sp),
                modifier = Modifier
                    .padding(2.dp)
                    .constrainAs(itemYear) {
                        start.linkTo(logo.end)
                        top.linkTo(itemType.bottom)
                        bottom.linkTo(itemTitle.top)
                    }
            )
            Text(
                text = "IMDB: ${movie.imdbID}",
                style = TextStyle.Default.copy(fontSize = 10.sp),
                modifier = Modifier
                    .padding(2.dp)
                    .constrainAs(itemImdb) {
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
    CircularProgressIndicator(
        Modifier
            .fillMaxSize()
            .semantics { testTag = "progressbar" })
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchScreen(hint: String, onSearch: (String) -> Unit) {
    val typedText = remember { mutableStateOf(TextFieldValue("")) }
    Column {
        Row(modifier = Modifier.padding(5.dp)) {
            Text(text = "Enter Movie Name to Search")
        }
        Row {
            TextField(
                value = typedText.value,
                onValueChange = { newTextValue: TextFieldValue ->
                    typedText.value = newTextValue
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics { testTag = "searchBar" },
                maxLines = 1,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Search
                ),
                placeholder = @Composable {
                    Text(text = hint)
                },
                keyboardActions = KeyboardActions {
                    onSearch(typedText.value.text)
                }
            )
        }
        Row(
            Modifier
                .fillMaxWidth()
                .padding(5.dp)) {
            Column {
                Button(modifier = Modifier.padding(5.dp), onClick = {
                    onSearch(typedText.value.text)
                }) {
                    Text(
                        text = "Search",
                        modifier = Modifier.semantics { testTag = "searchButton" }
                    )
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