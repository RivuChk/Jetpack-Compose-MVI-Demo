package dev.rivu.mvijetpackcomposedemo.moviesearch.presentation

import dev.rivu.mvijetpackcomposedemo.base.presentation.ISchedulerProvider
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers

class FakeSchedulerProvider(val testScheduler: Scheduler = Schedulers.trampoline()) :
    ISchedulerProvider {

    override fun io(): Scheduler = testScheduler

    override fun computation(): Scheduler = testScheduler

    override fun ui(): Scheduler = testScheduler

}