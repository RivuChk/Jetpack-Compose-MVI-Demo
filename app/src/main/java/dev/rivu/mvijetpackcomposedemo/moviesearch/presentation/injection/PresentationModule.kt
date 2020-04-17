package dev.rivu.mvijetpackcomposedemo.moviesearch.presentation.injection

import dev.rivu.mvijetpackcomposedemo.base.presentation.ISchedulerProvider
import dev.rivu.mvijetpackcomposedemo.moviesearch.presentation.MovieProcessor
import dev.rivu.mvijetpackcomposedemo.moviesearch.presentation.MovieViewModel
import dev.rivu.mvijetpackcomposedemo.moviesearch.presentation.SchedulerProvider
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {

    single<ISchedulerProvider> { SchedulerProvider() }

    single { MovieProcessor(get()) }
    viewModel { MovieViewModel(get(), get()) }

}