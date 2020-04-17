package dev.rivu.mvijetpackcomposedemo.moviesearch.data.remote.model


import com.google.gson.annotations.SerializedName

data class MovieDetailResponse(
    @SerializedName("Actors")
    val actors: String = "", // Jesse Eisenberg, Rooney Mara, Bryan Barter, Dustin Fitzsimons
    @SerializedName("Awards")
    val awards: String = "", // Won 3 Oscars. Another 171 wins & 183 nominations.
    @SerializedName("BoxOffice")
    val boxOffice: String = "", // $96,400,000
    @SerializedName("Country")
    val country: String = "", // USA
    @SerializedName("DVD")
    val dVD: String = "", // 11 Jan 2011
    @SerializedName("Director")
    val director: String = "", // David Fincher
    @SerializedName("Genre")
    val genre: String = "", // Biography, Drama
    @SerializedName("imdbID")
    val imdbID: String = "", // tt1285016
    @SerializedName("imdbRating")
    val imdbRating: String = "", // 7.7
    @SerializedName("imdbVotes")
    val imdbVotes: String = "", // 590,040
    @SerializedName("Language")
    val language: String = "", // English, French
    @SerializedName("Metascore")
    val metascore: String = "", // 95
    @SerializedName("Plot")
    val plot: String = "", // As Harvard student Mark Zuckerberg creates the social networking site that would become known as Facebook, he is sued by the twins who claimed he stole their idea, and by the co-founder who was later squeezed out of the business.
    @SerializedName("Poster")
    val poster: String = "", // https://m.media-amazon.com/images/M/MV5BOGUyZDUxZjEtMmIzMC00MzlmLTg4MGItZWJmMzBhZjE0Mjc1XkEyXkFqcGdeQXVyMTMxODk2OTU@._V1_SX300.jpg
    @SerializedName("Production")
    val production: String = "", // Columbia Pictures
    @SerializedName("Rated")
    val rated: String = "", // PG-13
    @SerializedName("Ratings")
    val ratings: List<Rating> = listOf(),
    @SerializedName("Released")
    val released: String = "", // 01 Oct 2010
    @SerializedName("Response")
    val response: String = "", // True
    @SerializedName("Runtime")
    val runtime: String = "", // 120 min
    @SerializedName("Title")
    val title: String = "", // The Social Network
    @SerializedName("Type")
    val type: String = "", // movie
    @SerializedName("Website")
    val website: String = "", // N/A
    @SerializedName("Writer")
    val writer: String = "", // Aaron Sorkin (screenplay), Ben Mezrich (book)
    @SerializedName("Year")
    val year: String = "" // 2010
) {
    data class Rating(
        @SerializedName("Source")
        val source: String = "", // Metacritic
        @SerializedName("Value")
        val value: String = "" // 95/100
    )
}