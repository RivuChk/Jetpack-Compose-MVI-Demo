package dev.rivu.mvijetpackcomposedemo.base.presentation

import dev.rivu.mvijetpackcomposedemo.base.MviAction
import dev.rivu.mvijetpackcomposedemo.base.MviResult
import io.reactivex.rxjava3.core.FlowableTransformer

interface MviActionProcessor<A: MviAction, R: MviResult> {
    val schedulerProvider: ISchedulerProvider

    fun transformFromAction(): FlowableTransformer<A, R>
}