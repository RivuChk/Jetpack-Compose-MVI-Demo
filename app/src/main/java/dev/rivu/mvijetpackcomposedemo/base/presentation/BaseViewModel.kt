package dev.rivu.mvijetpackcomposedemo.base.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.ViewModel
import dev.rivu.mvijetpackcomposedemo.base.*
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.FlowableTransformer
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.functions.BiFunction
import io.reactivex.rxjava3.subjects.PublishSubject

abstract class BaseViewModel<I : MviIntent, S : MviState, A : MviAction, R : MviResult> :
    ViewModel(),
    MviViewModel<I, S> {

    private val intentsSubject: PublishSubject<I> = PublishSubject.create()
    private val statesLiveData: LiveData<S> by lazy {
        LiveDataReactiveStreams.fromPublisher(statesObservable)
    }
    val statesObservable: Flowable<S> by lazy {
        compose()
    }

    abstract fun intentFilter(): FlowableTransformer<I, I>

    override fun processIntents(intents: Observable<I>) {
        intents.subscribe(intentsSubject)
    }

    override fun states(): LiveData<S> {
        return statesLiveData
    }

    abstract fun actionFromIntent(intent: I): A


    private fun compose(): Flowable<S> {
        return intentsSubject
            .toFlowable(BackpressureStrategy.LATEST)
            .compose(intentFilter())
            .map(this::actionFromIntent)
            .compose(actionProcessor.transformFromAction())
            .scan(initialState(), reducer())
            .distinctUntilChanged()
            .replay(1)
            .autoConnect(0)
    }

    protected abstract val actionProcessor: MviActionProcessor<A, R>

    abstract fun reducer(): BiFunction<S, R, S>

    abstract fun initialState(): S
}