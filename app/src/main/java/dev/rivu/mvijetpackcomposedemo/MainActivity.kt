package dev.rivu.mvijetpackcomposedemo

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.MaterialTheme
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint
import dev.rivu.mvijetpackcomposedemo.moviesearch.presentation.MovieIntent
import dev.rivu.mvijetpackcomposedemo.moviesearch.presentation.MovieViewModel
import dev.rivu.mvijetpackcomposedemo.moviesearch.presentation.isDetailState
import dev.rivu.mvijetpackcomposedemo.moviesearch.ui.MoviesScreen
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    val moviesViewModel: MovieViewModel by viewModels()

    val liveData by lazy {
        moviesViewModel.states()
    }

    val searchHistory = mutableListOf<String>()


    val searchPublisher: PublishSubject<MovieIntent.SearchIntent> = PublishSubject.create()
    val clickPublisher: PublishSubject<MovieIntent.ClickIntent> = PublishSubject.create()
    val clearClickPublisher: PublishSubject<MovieIntent.ClearClickIntent> = PublishSubject.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                MoviesScreen(liveData, ::search, ::click)
            }
        }

        //For logging
        liveData.observe(this, Observer {
            Timber.d("Updated State $it")
        })

        moviesViewModel.processIntents(intents())
    }

    private fun search(query: String) {
        searchHistory.add(query)
        searchWithoutHistory(query)
    }

    private fun searchWithoutHistory(query: String) {
        searchPublisher.onNext(MovieIntent.SearchIntent(query))
    }

    private fun click(imdbId: String) {
        clickPublisher.onNext(MovieIntent.ClickIntent(imdbId))
    }

    @OptIn(ExperimentalStdlibApi::class)
    override fun onBackPressed() {
        val state = liveData.value
        if (state != null && state.isDetailState()) {
            clearClickPublisher.onNext(MovieIntent.ClearClickIntent)
        } else {
            if (state?.query != null && searchHistory.contains(state.query)) {
                searchHistory.remove(state.query)
            }
            val pastSearch = searchHistory.removeLastOrNull()
            if (pastSearch != null) {
                searchWithoutHistory(pastSearch)
            } else {
                super.onBackPressed()
            }
        }
    }

    fun intents(): Observable<MovieIntent> {
        return Observable.merge(
            Observable.defer {
                Observable.just(
                    MovieIntent.InitialIntent
                )
            },
            searchPublisher.hide(),
            clickPublisher.hide(),
            clearClickPublisher.hide()
        )
    }
}