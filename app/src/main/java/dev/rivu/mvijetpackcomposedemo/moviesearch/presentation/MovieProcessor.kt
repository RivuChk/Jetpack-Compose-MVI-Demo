package dev.rivu.mvijetpackcomposedemo.moviesearch.presentation

import dev.rivu.mvijetpackcomposedemo.base.presentation.ISchedulerProvider
import dev.rivu.mvijetpackcomposedemo.base.presentation.MviActionProcessor
import dev.rivu.mvijetpackcomposedemo.moviesearch.data.IMovieRepository
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.FlowableTransformer

open class MovieProcessor(
    private val repository: IMovieRepository,
    override val schedulerProvider: ISchedulerProvider
) :
    MviActionProcessor<MovieAction, MovieResult> {
    override fun transformFromAction(): FlowableTransformer<MovieAction, MovieResult> =
        FlowableTransformer { actionFlowable ->
            actionFlowable.publish { shared ->
                Flowable.merge(
                    shared.ofType(MovieAction.SearchAction::class.java).compose(searchMovies()),
                    shared.ofType(MovieAction.DetailAction::class.java).compose(loadDetails()),
                    shared.ofType(MovieAction.ClearDetailAction::class.java)
                        .compose(clearDetails()),
                    shared.ofType(MovieAction.InitAction::class.java).compose(initUi())
                )
            }
        }

    private fun searchMovies(): FlowableTransformer<MovieAction.SearchAction, MovieResult.SearchResult> =
        FlowableTransformer { actionFlowable ->
            actionFlowable.switchMap { action ->
                repository.getMovies(action.query)
                    .map {
                        MovieResult.SearchResult.Success(
                            movies = it,
                            query = action.query
                        )
                    }
                    .cast(MovieResult.SearchResult::class.java)
                    .onErrorReturn { error ->
                        MovieResult.SearchResult.Failure(
                            error = error,
                            query = action.query
                        )
                    }
                    .subscribeOn(schedulerProvider.io())
                    .observeOn(schedulerProvider.ui())
                    .startWithItem(
                        MovieResult.SearchResult.InProgress(action.query)
                    )
            }
        }

    private fun loadDetails(): FlowableTransformer<MovieAction.DetailAction, MovieResult.LoadDetailResult> =
        FlowableTransformer { actionFlowable ->
            actionFlowable.switchMap { action ->
                repository.getMovieDetail(action.imdbId)
                    .map {
                        MovieResult.LoadDetailResult.Success(
                            movieDetail = it,
                            imdbId = action.imdbId
                        )
                    }
                    .cast(MovieResult.LoadDetailResult::class.java)
                    .onErrorReturn { error ->
                        MovieResult.LoadDetailResult.Failure(
                            error = error,
                            imdbId = action.imdbId
                        )
                    }
                    .subscribeOn(schedulerProvider.io())
                    .observeOn(schedulerProvider.ui())
                    .startWithItem(
                        MovieResult.LoadDetailResult.InProgress(action.imdbId)
                    )
            }
        }

    private fun clearDetails(): FlowableTransformer<MovieAction.ClearDetailAction, MovieResult.ClearDetailResult> =
        FlowableTransformer {
            it.switchMap {
                Flowable.just(MovieResult.ClearDetailResult)
            }
        }

    private fun initUi(): FlowableTransformer<MovieAction.InitAction, MovieResult.InitResult> =
        FlowableTransformer {
            it.switchMap {
                Flowable.just(MovieResult.InitResult)
            }
        }
}