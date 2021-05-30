package com.example.studycase.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.studycase.data.dao.PopularMoviesDao
import com.example.studycase.data.model.PopulerMovieResultItem

@Database(entities = [PopulerMovieResultItem::class],version = 1)
abstract class PopularMovieDatabase :RoomDatabase() {
    abstract fun popularMovieDao(): PopularMoviesDao
}