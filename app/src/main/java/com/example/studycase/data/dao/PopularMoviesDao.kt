package com.example.studycase.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.studycase.data.model.PopulerMovieResultItem
import kotlinx.coroutines.flow.Flow

@Dao
interface PopularMoviesDao {

    @Query("SELECT * from popularmovies")
    suspend fun getAllPopularMovies(): List<PopulerMovieResultItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPopularMovies(popularMovies: List<PopulerMovieResultItem>)

    @Query("DELETE FROM popularmovies")
    suspend fun deleteAllPopularMovies()
}