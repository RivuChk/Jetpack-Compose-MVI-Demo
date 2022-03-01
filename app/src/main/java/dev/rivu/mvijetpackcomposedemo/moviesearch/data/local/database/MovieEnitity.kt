package dev.rivu.mvijetpackcomposedemo.moviesearch.data.local.database

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class MovieEnitity(
    @PrimaryKey
    var imdbID: String = "", // tt7728344
    var poster: String = "", // https://m.media-amazon.com/images/M/MV5BZGU5YTVlZTktNzgzMS00MGVlLTgyMGMtNWYwNTkwNGY1MzllXkEyXkFqcGdeQXVyNTAyODkwOQ@@._V1_SX300.jpg
    var title: String = "", // Marvel Rising: Secret Warriors
    var type: String = "", // movie
    var year: String = "",
    @Embedded
    var detail: Detail? = null

) {
    data class Detail(
        var actors: List<String> = emptyList(), // Jesse Eisenberg, Rooney Mara, Bryan Barter, Dustin Fitzsimons
        var awards: String = "", // Won 3 Oscars. Another 171 wins & 183 nominations.
        var boxOffice: String = "", // $96,400,000
        var country: String = "", // USA
        var dVD: String = "", // 11 Jan 2011
        var director: String = "", // David Fincher
        var genre: String = "", // Biography, Drama
        var imdbRating: String = "", // 7.7
        var imdbVotes: String = "", // 590,040
        var language: String = "", // English, French
        var metascore: String = "", // 95
        var plot: String = "", // As Harvard student Mark Zuckerberg creates the social networking site that would become known as Facebook, he is sued by the twins who claimed he stole their idea, and by the co-founder who was later squeezed out of the business.
        var production: String = "", // Columbia Pictures
        var rated: String = "", // PG-13
        var ratings: List<Rating> = listOf(),
        var released: String = "", // 01 Oct 2010
        var response: String = "", // True
        var runtime: String = "", // 120 min
        var website: String = "", // N/A
        var writer: String = "" // Aaron Sorkin (screenplay), Ben Mezrich (book)
    ) {
        data class Rating(
            var source: String = "", // Metacritic
            var value: String = "" // 95/100
        )
    }
}