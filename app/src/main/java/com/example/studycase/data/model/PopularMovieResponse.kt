package com.example.studycase.data.model


data class PopularMovieResponse(
	val page: Int?,
	val totalPages: Int?,
	val results: List<PopulerMovieResultItem?>?,
	val totalResults: Int?
)

