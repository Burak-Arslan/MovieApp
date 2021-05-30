package com.example.studycase.features.home

import androidx.lifecycle.*
import com.example.studycase.data.model.PopulerMovieResultItem
import com.example.studycase.data.repository.PopularMovieRepository
import com.example.testappp.data.state.StateLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: PopularMovieRepository) :
    ViewModel() {

    var language = ""
    var page = 1


    fun deleteAllMovies() {
        viewModelScope.launch {
            repository.deleteAllPopulerMoviesData()
        }
    }

    fun getAllMovies(): List<PopulerMovieResultItem>? {
        var listMovie: List<PopulerMovieResultItem>? = null
        runBlocking{
            listMovie = repository.getAllPopularMovieData()
        }
        return listMovie
    }

    public val popularMovies =
        StateLiveData(viewModelScope.coroutineContext + Dispatchers.Main) {
            repository.getAllPopularMovieApi(language, page)
        }

    fun insertPopularMovie(popularMovie: List<PopulerMovieResultItem>) {
        viewModelScope.launch {
            repository.insertPopularMovie(popularMovie)
        }
    }
}