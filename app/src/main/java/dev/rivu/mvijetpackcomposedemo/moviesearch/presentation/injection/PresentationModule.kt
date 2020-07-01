package dev.rivu.mvijetpackcomposedemo.moviesearch.presentation.injection

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dev.rivu.mvijetpackcomposedemo.base.presentation.ISchedulerProvider
import dev.rivu.mvijetpackcomposedemo.moviesearch.data.IMovieRepository
import dev.rivu.mvijetpackcomposedemo.moviesearch.presentation.MovieProcessor

@Module
@InstallIn(ActivityComponent::class)
object PresentationModule {

    @Provides
    fun provideMovieProcessor(repository: IMovieRepository, schedulerProvider: ISchedulerProvider) = MovieProcessor(repository, schedulerProvider)

}