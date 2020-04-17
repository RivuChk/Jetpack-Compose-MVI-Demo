package dev.rivu.mvijetpackcomposedemo.moviesearch.presentation

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers

interface ISchedulerProvider {
    fun io(): Scheduler
    fun computation(): Scheduler
    fun ui(): Scheduler
}

class SchedulerProvider: ISchedulerProvider {
    override fun io(): Scheduler = Schedulers.io()

    override fun computation(): Scheduler = Schedulers.computation()

    override fun ui(): Scheduler = AndroidSchedulers.mainThread()

}