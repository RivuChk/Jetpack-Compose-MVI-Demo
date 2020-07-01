package dev.rivu.mvijetpackcomposedemo.moviesearch.data.injection

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.rivu.mvijetpackcomposedemo.moviesearch.data.IMovieRepository
import dev.rivu.mvijetpackcomposedemo.moviesearch.data.MovieDataStore
import dev.rivu.mvijetpackcomposedemo.moviesearch.data.MovieRepository
import dev.rivu.mvijetpackcomposedemo.moviesearch.data.local.LocalMovieDataStore
import dev.rivu.mvijetpackcomposedemo.moviesearch.data.local.database.MovieDB
import dev.rivu.mvijetpackcomposedemo.moviesearch.data.local.database.MovieDao
import dev.rivu.mvijetpackcomposedemo.moviesearch.data.remote.MovieApi
import dev.rivu.mvijetpackcomposedemo.moviesearch.data.remote.MovieApiFactory
import dev.rivu.mvijetpackcomposedemo.moviesearch.data.remote.RemoteMovieDataStore
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(ActivityComponent::class)
object DataModule {


    @Provides
    @Named("local")
    fun provideLocalMovieDataStore(dao: MovieDao): MovieDataStore = LocalMovieDataStore(dao)


    @Provides
    @Named("remote")
    fun provideRemoteMovieDataStore(api: MovieApi): MovieDataStore = RemoteMovieDataStore(api)


    @Provides
    fun provideMovieRepository(
        @Named("local") localMovieDataStore: MovieDataStore,
        @Named("remote") remoteMovieDataStore: MovieDataStore
    ): IMovieRepository = MovieRepository(localMovieDataStore, remoteMovieDataStore)
}

@Module
@InstallIn(ApplicationComponent::class)
object AppDataModule {

    @Provides
    @Singleton
    fun provideMovieApi(): MovieApi = MovieApiFactory.makeMovieApi()


    @Provides
    @Singleton
    fun provideDB(@ApplicationContext context: Context): MovieDB = MovieDB.getInstance(context)


    @Provides
    @Singleton
    fun provideMovieDao(db: MovieDB): MovieDao = db.movieDao()


}