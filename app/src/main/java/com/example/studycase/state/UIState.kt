package com.example.testappp.data.state

sealed class UIState<out R> {
    data class Loading(val isLoading: Boolean) : UIState<Nothing>()
    object Retrying : UIState<Nothing>()
    object SwipeRefreshing : UIState<Nothing>()
    data class Success<T>(val data: T, val headers: okhttp3.Headers?) : UIState<T>()
    data class SuccessWithFileName<T>(val data: T, val fileName: String) : UIState<T>()
    data class Failure(val exception: Exception) : UIState<Nothing>()
    data class SwipeRefreshFailure(val exception: Exception) : UIState<Nothing>()
}