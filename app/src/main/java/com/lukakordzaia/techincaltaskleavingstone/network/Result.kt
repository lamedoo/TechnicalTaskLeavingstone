package com.lukakordzaia.techincaltaskleavingstone.network

sealed class Result<out T: Any> {
    data class Success<out T : Any>(val data: T) : Result<T>()
    data class Error(val exception: String) : Result<Nothing>()
    data class Internet(val isInternet: Boolean) : Result<Nothing>()
}