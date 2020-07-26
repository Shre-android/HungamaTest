package com.e.hungamatest.model.db.pojo

import com.google.gson.annotations.SerializedName

data class SpokenLanguagesItem(val name: String = "",
                               val iso: String = "")


data class GenresItem(val name: String? = "",
                      val id: Int = 0)


data class ProductionCountriesItem(val iso: String = "",
                                   val name: String = "")


data class ProductionCompaniesItem(val logoPath: String? = null,
                                   val name: String = "",
                                   val id: Int = 0,
                                   val originCountry: String = "")


data class MovieDetail(val originalLanguage: String = "",
                       val imdbId: String = "",
                       val video: Boolean = false,
                       val title: String = "",
                       val backdropPath: String = "",
                       val revenue: Int = 0,
                       val genres: List<GenresItem>?,
                       val popularity: Double = 0.0,
                       @SerializedName("production_countries")
                       val productionCountries: List<ProductionCountriesItem>?,
                       val id: Int = 0,
                       @SerializedName("vote_count")
                       val voteCount: Int = 0,
                       val budget: Int = 0,
                       val overview: String = "",
                       val originalTitle: String = "",
                       val runtime: Int = 0,
                       @SerializedName("poster_path")
                       val posterPath: String = "",
                       @SerializedName("spoken_languages")
                       val spokenLanguages: List<SpokenLanguagesItem>?,
                       @SerializedName("production_companies")
                       val productionCompanies: List<ProductionCompaniesItem>?,
                       @SerializedName("release_date")
                       val releaseDate: String = "",
                       @SerializedName("vote_average")
                       val voteAverage: Double = 0.0,
                       val tagline: String = "",
                       val adult: Boolean = false,
                       val homepage: String = "",
                       val status: String = "")


