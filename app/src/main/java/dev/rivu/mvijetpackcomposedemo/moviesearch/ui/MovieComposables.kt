package dev.rivu.mvijetpackcomposedemo.moviesearch.ui

import androidx.compose.Composable
import androidx.lifecycle.LiveData
import androidx.ui.foundation.Text
import androidx.compose.getValue
import androidx.ui.livedata.observeAsState
import dev.rivu.mvijetpackcomposedemo.moviesearch.presentation.MoviesState

@Composable
fun MoviesScreen(stateLiveData: LiveData<MoviesState>) {
    val state: MoviesState by stateLiveData.observeAsState(MoviesState.initialState())

    Text("Updated State $state")
}