package dev.rivu.mvijetpackcomposedemo.moviesearch.data.injection

import dev.rivu.mvijetpackcomposedemo.moviesearch.data.IMovieRepository
import dev.rivu.mvijetpackcomposedemo.moviesearch.data.MovieDataStore
import dev.rivu.mvijetpackcomposedemo.moviesearch.data.MovieRepository
import dev.rivu.mvijetpackcomposedemo.moviesearch.data.local.LocalMovieDataStore
import dev.rivu.mvijetpackcomposedemo.moviesearch.data.remote.MovieApi
import dev.rivu.mvijetpackcomposedemo.moviesearch.data.remote.MovieApiFactory
import dev.rivu.mvijetpackcomposedemo.moviesearch.data.remote.RemoteMovieDataStore
import org.koin.core.qualifier.named
import org.koin.dsl.module

val dataModule = module {

    single { MovieApiFactory.makeMovieApi() }

    single<MovieDataStore>(named("local")) { LocalMovieDataStore() }
    single<MovieDataStore>(named("remote")) { RemoteMovieDataStore(get()) }

    single<IMovieRepository> { MovieRepository(get(named("local")), get(named("remote"))) }
}