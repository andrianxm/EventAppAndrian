package com.andrian.mydicodingeventapp.data

sealed class Result<out R> private constructor() {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: String) : Result<Nothing>()
    data object Loading : Result<Nothing>()
}