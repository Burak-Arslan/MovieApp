package com.example.studycase.data.repository


import com.example.studycase.api.Api
import com.example.studycase.data.PopularMovieDatabase
import com.example.studycase.data.model.PopularMovieResponse
import com.example.studycase.data.model.PopulerMovieResultItem
import retrofit2.Response
import javax.inject.Inject

class PopularMovieRepository @Inject constructor(

    private val apiService: Api,
    private val localDataRepository: PopularMovieDatabase
) {

    private val apiKey = "3d9702c19b753c0cdee1ad0df68b885e"

    private val localDatabase = localDataRepository.popularMovieDao()

    suspend fun deleteAllPopulerMoviesData(){
        return localDatabase.deleteAllPopularMovies()
    }
    suspend fun getAllPopularMovieData(): List<PopulerMovieResultItem> {
        return localDatabase.getAllPopularMovies()
    }

    suspend fun insertPopularMovie(popularMovie: List<PopulerMovieResultItem>) {
        return localDatabase.insertPopularMovies(popularMovie)
    }

    suspend fun getAllPopularMovieApi(
        language: String,
        page: Int
    ): Response<PopularMovieResponse> {
        return apiService.getPopularMovies(apiKey, language, page)
    }
}
