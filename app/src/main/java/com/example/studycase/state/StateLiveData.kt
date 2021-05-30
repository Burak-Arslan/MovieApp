package com.example.testappp.data.state

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import retrofit2.Response
import kotlin.coroutines.CoroutineContext


class StateLiveData<T>(
    private val coroutineContext: CoroutineContext,
    fetchData: (suspend () -> Response<T>)
) {
    private val action = MutableLiveData<Action>()
    var data: T? = null // backing data
    var fileName: String? = null
    val state = action.switchMap {
        liveData(context = coroutineContext) {
            when (action.value) {
                Action.Load -> {
                    emit(UIState.Loading(true))
                }

                Action.SwipeRefresh -> {
                    emit(UIState.SwipeRefreshing)
                }

                Action.Retry -> {
                    emit(UIState.Retrying)
                }
            }

            try {
                val response = fetchData()
                val body = response.body()
                when {
                    response.isSuccessful && body != null -> {
                        data = body
                        fileName = response.headers()["file-name"]
                        if (!fileName.isNullOrEmpty()) {
                            emit(UIState.SuccessWithFileName<T>(body, fileName!!))
                        } else {
                            emit(UIState.Success<T>(body, response.headers()))
                        }

                        emit(UIState.Loading(false))
                    }
                    action.value == Action.SwipeRefresh -> {
                        emit(UIState.SwipeRefreshFailure(Exception()))
                        emit(UIState.Loading(false))
                    }
                    else -> {
                        val exception = Exception(response.errorBody()?.string())
                        emit(UIState.Failure(Exception(exception)))
                        emit(UIState.Loading(false))
                    }
                }
            } catch (exception: Exception) {
                when {
                    action.value == Action.SwipeRefresh -> {
                        emit(UIState.SwipeRefreshFailure(Exception(exception)))
                        data?.let {
                            // emit success with existing data
                            emit(UIState.Success(it, null))
                            emit(UIState.Loading(false))
                        }
                    }
                    else -> {
                        emit(UIState.Failure(Exception(exception)))
                        emit(UIState.Loading(false))
                    }
                }
            }
        }
    }

    // Helpers for triggering different actions

    fun retry() {
        action.value = Action.Retry
    }

    fun swipeRefresh() {
        action.value = Action.SwipeRefresh
    }

    fun load() {
        action.value = Action.Load
    }
}