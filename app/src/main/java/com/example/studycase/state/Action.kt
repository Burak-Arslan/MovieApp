package com.example.testappp.data.state

sealed class Action {
    object Load : Action()
    object SwipeRefresh : Action()
    object Retry : Action()
}