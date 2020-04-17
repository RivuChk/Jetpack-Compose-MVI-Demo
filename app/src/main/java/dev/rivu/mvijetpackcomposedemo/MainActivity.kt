package dev.rivu.mvijetpackcomposedemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.ui.core.setContent
import androidx.ui.material.MaterialTheme
import dev.rivu.mvijetpackcomposedemo.moviesearch.presentation.MovieIntent
import dev.rivu.mvijetpackcomposedemo.moviesearch.presentation.MovieViewModel
import dev.rivu.mvijetpackcomposedemo.moviesearch.ui.MoviesScreen
import io.reactivex.rxjava3.core.Observable
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    val moviesViewModel: MovieViewModel by viewModel()

    val liveData by lazy {
        moviesViewModel.states()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                MoviesScreen(liveData)
            }
        }

        //For logging
        liveData.observe(this, Observer {
            Timber.d("Updated State $it")
        })

        moviesViewModel.processIntents(intents())
    }

    fun intents(): Observable<MovieIntent> {
        return Observable.defer {
            Observable.just(
                MovieIntent.InitialIntent,
                MovieIntent.SearchIntent("John"),
                MovieIntent.SearchIntent("Jack")
            )
        }
    }
}