package dev.rivu.mvijetpackcomposedemo.base.presentation

import dev.rivu.mvijetpackcomposedemo.base.MviAction
import dev.rivu.mvijetpackcomposedemo.base.MviResult
import io.reactivex.rxjava3.core.FlowableTransformer

interface MviActionProcessor<A: MviAction, R: MviResult> {
    fun transformFromAction(): FlowableTransformer<A, R>
}