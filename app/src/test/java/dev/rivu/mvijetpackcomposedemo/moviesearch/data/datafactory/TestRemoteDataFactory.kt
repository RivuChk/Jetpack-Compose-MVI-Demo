package dev.rivu.mvijetpackcomposedemo.moviesearch.data.datafactory

import dev.rivu.mvijetpackcomposedemo.moviesearch.data.model.MovieDetail
import dev.rivu.mvijetpackcomposedemo.moviesearch.data.remote.model.MovieDetailResponse
import dev.rivu.mvijetpackcomposedemo.moviesearch.data.remote.model.MovieSearchResponse
import kotlin.random.Random

val dummyMovieSearchResponse: MovieSearchResponse
    get() = MovieSearchResponse(
        response = "",
        totalResults = "",
        movies = (0..10).toList()
            .map {
                MovieSearchResponse.Movie(
                    imdbID = "$it",
                    title = "Title $it",
                    poster = "Poster $it",
                    type = "Type $it",
                    year = "${2000 - it}"
                )
            }
    )

val dummyMovieDetailResponse: MovieDetailResponse
    get() = MovieDetailResponse(
        response = "",
        actors = "Jesse Eisenberg, Rooney Mara, Bryan Barter, Dustin Fitzsimons",
        awards = "Won 3 Oscars. Another 171 wins & 183 nominations.",
        boxOffice = "$96,400,000",
        country = "USA",
        dVD = "11 Jan 2011",
        director = "David Fincher",
        genre = "Biography, Drama",
        imdbID = "tt1285016",
        imdbRating = "7.7",
        imdbVotes = "590,040",
        language = "English, French",
        metascore = "95",
        plot = "As Harvard student Mark Zuckerberg creates the social networking site that would become known as Facebook, he is sued by the twins who claimed he stole their idea, and by the co-founder who was later squeezed out of the business.",
        poster = "https://m.media-amazon.com/images/M/MV5BOGUyZDUxZjEtMmIzMC00MzlmLTg4MGItZWJmMzBhZjE0Mjc1XkEyXkFqcGdeQXVyMTMxODk2OTU@._V1_SX300.jpg",
        production = "Columbia Pictures",
        rated = "PG-13",
        ratings = listOf(),
        released = "01 Oct 2010",
        runtime = "120 min",
        title = "The Social Network",
        type = "movie",
        website = "N/A",
        writer = "Aaron Sorkin (screenplay), Ben Mezrich (book)",
        year = "2010"
    )

val dummyMovieDetail: MovieDetail
    get() = MovieDetail(
        response = "",
        actors = "Jesse Eisenberg, Rooney Mara, Bryan Barter, Dustin Fitzsimons".split(", ", ignoreCase = true),
        awards = "Won 3 Oscars. Another 171 wins & 183 nominations.",
        boxOffice = "$96,400,000",
        country = "USA",
        dVD = "11 Jan 2011",
        director = "David Fincher",
        genre = "Biography, Drama",
        imdbID = "tt1285016",
        imdbRating = "7.7",
        imdbVotes = "590,040",
        language = "English, French",
        metascore = "95",
        plot = "As Harvard student Mark Zuckerberg creates the social networking site that would become known as Facebook, he is sued by the twins who claimed he stole their idea, and by the co-founder who was later squeezed out of the business.",
        poster = "https://m.media-amazon.com/images/M/MV5BOGUyZDUxZjEtMmIzMC00MzlmLTg4MGItZWJmMzBhZjE0Mjc1XkEyXkFqcGdeQXVyMTMxODk2OTU@._V1_SX300.jpg",
        production = "Columbia Pictures",
        rated = "PG-13",
        ratings = listOf(),
        released = "01 Oct 2010",
        runtime = "120 min",
        title = "The Social Network",
        type = "movie",
        website = "N/A",
        writer = "Aaron Sorkin (screenplay), Ben Mezrich (book)",
        year = "2010"
    )