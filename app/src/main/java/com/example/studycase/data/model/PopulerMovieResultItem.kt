package com.example.studycase.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "popularmovies")
data class PopulerMovieResultItem(
    val overview: String?,
    val originalLanguage: String?,
    val originalTitle: String?,
    val video: Boolean?,
    val title: String?,
    //val genreIds: List<Int?>?,
    val original_language: String?,
    val original_title: String?,
    val poster_path: String?,
    val release_date: String?,
    val vote_average: Double?,
    val vote_count: Double?,
    val backdrop_path: String?,
    val backdropPath: String?,
    val popularity: Double?,
    val voteAverage: Double?,
    @PrimaryKey
    val id: Int?,
    val adult: Boolean?,
    val voteCount: Int?
)
