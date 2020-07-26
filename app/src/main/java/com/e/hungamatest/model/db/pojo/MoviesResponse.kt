package com.e.hungamatest.model.db.pojo

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "movie")
data class Movie(val overview: String = "",
                 @SerializedName("original_language")
                 val originalLanguage: String = "",
                 val originalTitle: String = "",
                 val video: Boolean = false,
                 val title: String = "",
                 @SerializedName("poster_path")
                 val posterPath: String = "",
                 val backdropPath: String = "",
                 @SerializedName("release_date")
                 val releaseDate: String = "",
                 val popularity: Double = 0.0,
                 @SerializedName("vote_average")
                 val voteAverage: Double = 0.0,
                 @PrimaryKey(autoGenerate = false) val id: Int = 0,
                 val adult: Boolean = false,
                 var isFavourite:Boolean = false,
                 val voteCount: Int = 0)


data class MoviesResponse(val page: Int = 0,
                          @SerializedName("total_pages")
                          val totalPages: Int = 0,
                          val results: List<Movie>?,
                          @SerializedName("total_results")
                          val totalResults: Int = 0)


