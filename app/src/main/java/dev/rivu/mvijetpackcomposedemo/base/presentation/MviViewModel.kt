package dev.rivu.mvijetpackcomposedemo.base.presentation

import androidx.lifecycle.LiveData
import dev.rivu.mvijetpackcomposedemo.base.MviIntent
import dev.rivu.mvijetpackcomposedemo.base.MviState
import io.reactivex.rxjava3.core.Observable

interface MviViewModel<I : MviIntent, S : MviState> {
    fun processIntents(intents: Observable<I>)

    fun states(): LiveData<S>
}