package dev.rivu.mvijetpackcomposedemo.moviesearch.data.datafactory

import dev.rivu.mvijetpackcomposedemo.moviesearch.data.local.database.MovieEnitity
import dev.rivu.mvijetpackcomposedemo.moviesearch.data.model.Movie
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
                val random = Random(it).nextInt(1,100)
                MovieSearchResponse.Movie(
                    imdbID = "$it$random",
                    title = "Title $it $random",
                    poster = "Poster $it $random",
                    type = "Type $it $random",
                    year = "${2000 - it}"
                )
            }
    )

val dummyMovieSearchList: List<Movie>
    get() = (0..10).toList()
        .map {
            val random = Random(it).nextInt(1,100)
            Movie(
                imdbID = "$it$random",
                title = "Title $it $random",
                poster = "Poster $it $random",
                type = "Type $it $random",
                year = "${2000 - it}"
            )
        }

val dummyMovieEntityList: List<MovieEnitity>
    get() = (0..10).toList()
        .map {
            getDummyMovieEntity(it)
        }

val dummyMovieDetailResponse: MovieDetailResponse
    get() {
        val random = Random(100).nextInt(1,100)
        return MovieDetailResponse(
            response = "",
            actors = "Jesse Eisenberg, Rooney Mara, Bryan Barter, Dustin Fitzsimons",
            awards = "Won 3 Oscars. Another 171 wins & 183 nominations.",
            boxOffice = "$96,400,000,$random",
            country = "USA",
            dVD = "11 Jan 2011",
            director = "David Fincher",
            genre = "Biography, Drama",
            imdbID = "tt1285016$random",
            imdbRating = "7.7",
            imdbVotes = "590,040",
            language = "English, French",
            metascore = "95",
            plot = "$random As Harvard student Mark Zuckerberg creates the social networking site that would become known as Facebook, he is sued by the twins who claimed he stole their idea, and by the co-founder who was later squeezed out of the business.",
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
    }

val dummyMovieDetail: MovieDetail
    get() {
        val random = Random(100).nextInt(1,100)
        return MovieDetail(
            response = "",
            actors = "Jesse Eisenberg, Rooney Mara, Bryan Barter, Dustin Fitzsimons".split(", "),
            awards = "Won 3 Oscars. Another 171 wins & 183 nominations.",
            boxOffice = "$96,400,000,$random",
            country = "USA",
            dVD = "11 Jan 2011",
            director = "David Fincher",
            genre = "Biography, Drama",
            imdbID = "tt1285016$random",
            imdbRating = "7.7",
            imdbVotes = "590,040",
            language = "English, French",
            metascore = "95",
            plot = "$random As Harvard student Mark Zuckerberg creates the social networking site that would become known as Facebook, he is sued by the twins who claimed he stole their idea, and by the co-founder who was later squeezed out of the business.",
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
    }

fun getDummyMovieEntity(itemNumber: Int): MovieEnitity {
        val random = Random(itemNumber).nextInt(1,100)
        return MovieEnitity(
            imdbID = "tt1285016$random$itemNumber",
            poster = "https://m.media-amazon.com/images/M/MV5BOGUyZDUxZjEtMmIzMC00MzlmLTg4MGItZWJmMzBhZjE0Mjc1XkEyXkFqcGdeQXVyMTMxODk2OTU@._V1_SX300.jpg",
            title = "The Social Network",
            type = "movie",
            year = "2010",
            detail = MovieEnitity.Detail(
                response = "",
                actors = "Jesse Eisenberg, Rooney Mara, Bryan Barter, Dustin Fitzsimons".split(", "),
                awards = "Won 3 Oscars. Another 171 wins & 183 nominations.",
                boxOffice = "$96,400,000,$random",
                country = "USA",
                dVD = "11 Jan 2011",
                director = "David Fincher",
                genre = "Biography, Drama",
                imdbRating = "7.7",
                imdbVotes = "590,040",
                language = "English, French",
                metascore = "${itemNumber + random}",
                plot = "${itemNumber + random} As Harvard student Mark Zuckerberg creates the social networking site that would become known as Facebook, he is sued by the twins who claimed he stole their idea, and by the co-founder who was later squeezed out of the business.",
                production = "Columbia Pictures",
                rated = "PG-13",
                ratings = listOf(),
                released = "01 Oct 2010",
                runtime = "120 min",
                website = "N/A",
                writer = "Aaron Sorkin (screenplay), Ben Mezrich (book)"
            )
        )
    }