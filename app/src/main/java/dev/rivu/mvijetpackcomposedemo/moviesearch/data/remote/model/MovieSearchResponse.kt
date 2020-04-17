package dev.rivu.mvijetpackcomposedemo.moviesearch.data.remote.model


import com.google.gson.annotations.SerializedName

data class MovieSearchResponse(
    @SerializedName("Response")
    val response: String = "", // True
    @SerializedName("Search")
    val movies: List<Movie> = listOf(),
    @SerializedName("totalResults")
    val totalResults: String = "" // 96
) {
    data class Movie(
        @SerializedName("imdbID")
        val imdbID: String = "", // tt7728344
        @SerializedName("Poster")
        val poster: String = "", // https://m.media-amazon.com/images/M/MV5BZGU5YTVlZTktNzgzMS00MGVlLTgyMGMtNWYwNTkwNGY1MzllXkEyXkFqcGdeQXVyNTAyODkwOQ@@._V1_SX300.jpg
        @SerializedName("Title")
        val title: String = "", // Marvel Rising: Secret Warriors
        @SerializedName("Type")
        val type: String = "", // movie
        @SerializedName("Year")
        val year: String = "" // 2018
    )
}