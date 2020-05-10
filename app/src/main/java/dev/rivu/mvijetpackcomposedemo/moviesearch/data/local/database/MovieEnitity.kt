package dev.rivu.mvijetpackcomposedemo.moviesearch.data.local.database

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity(tableName = "movies")
data class MovieEnitity(
    @PrimaryKey
    val imdbID: String = "", // tt7728344
    val poster: String = "", // https://m.media-amazon.com/images/M/MV5BZGU5YTVlZTktNzgzMS00MGVlLTgyMGMtNWYwNTkwNGY1MzllXkEyXkFqcGdeQXVyNTAyODkwOQ@@._V1_SX300.jpg
    val title: String = "", // Marvel Rising: Secret Warriors
    val type: String = "", // movie
    val year: String = "",
    @Embedded
    val detail: Detail? = null

) {
    data class Detail(
        val actors: List<String> = emptyList(), // Jesse Eisenberg, Rooney Mara, Bryan Barter, Dustin Fitzsimons
        val awards: String = "", // Won 3 Oscars. Another 171 wins & 183 nominations.
        val boxOffice: String = "", // $96,400,000
        val country: String = "", // USA
        val dVD: String = "", // 11 Jan 2011
        val director: String = "", // David Fincher
        val genre: String = "", // Biography, Drama
        val imdbRating: String = "", // 7.7
        val imdbVotes: String = "", // 590,040
        val language: String = "", // English, French
        val metascore: String = "", // 95
        val plot: String = "", // As Harvard student Mark Zuckerberg creates the social networking site that would become known as Facebook, he is sued by the twins who claimed he stole their idea, and by the co-founder who was later squeezed out of the business.
        val production: String = "", // Columbia Pictures
        val rated: String = "", // PG-13
        val ratings: List<Rating> = listOf(),
        val released: String = "", // 01 Oct 2010
        val response: String = "", // True
        val runtime: String = "", // 120 min
        val website: String = "", // N/A
        val writer: String = "" // Aaron Sorkin (screenplay), Ben Mezrich (book)
    ) {
        @Serializable
        data class Rating(
            val source: String = "", // Metacritic
            val value: String = "" // 95/100
        )
    }
}