package dev.rivu.mvijetpackcomposedemo.moviesearch.data.model


data class MovieDetail(
    val actors: List<String> = emptyList(), // Jesse Eisenberg, Rooney Mara, Bryan Barter, Dustin Fitzsimons
    val awards: String = "", // Won 3 Oscars. Another 171 wins & 183 nominations.
    val boxOffice: String = "", // $96,400,000
    val country: String = "", // USA
    val dVD: String = "", // 11 Jan 2011
    val director: String = "", // David Fincher
    val genre: String = "", // Biography, Drama
    val imdbID: String = "", // tt1285016
    val imdbRating: String = "", // 7.7
    val imdbVotes: String = "", // 590,040
    val language: String = "", // English, French
    val metascore: String = "", // 95
    val plot: String = "", // As Harvard student Mark Zuckerberg creates the social networking site that would become known as Facebook, he is sued by the twins who claimed he stole their idea, and by the co-founder who was later squeezed out of the business.
    val poster: String = "", // https://m.media-amazon.com/images/M/MV5BOGUyZDUxZjEtMmIzMC00MzlmLTg4MGItZWJmMzBhZjE0Mjc1XkEyXkFqcGdeQXVyMTMxODk2OTU@._V1_SX300.jpg
    val production: String = "", // Columbia Pictures
    val rated: String = "", // PG-13
    val ratings: List<Rating> = listOf(),
    val released: String = "", // 01 Oct 2010
    val response: String = "", // True
    val runtime: String = "", // 120 min
    val title: String = "", // The Social Network
    val type: String = "", // movie
    val website: String = "", // N/A
    val writer: String = "", // Aaron Sorkin (screenplay), Ben Mezrich (book)
    val year: String = "" // 2010
) {
    data class Rating(
        val source: String = "", // Metacritic
        val value: String = "" // 95/100
    )
}