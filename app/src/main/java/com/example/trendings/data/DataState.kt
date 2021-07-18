package com.example.trendings.data

/**
 * A Sealed class to fetch data from server which will be either data or the error.
 * @author MaliK Dawar
 */
sealed class DataState<T> {
    data class Success<T>(val data: T): DataState<T>()
    data class Error<T>(val message: String): DataState<T>()

    companion object {
        fun <T> success(data: T) = Success<T>(data)
        fun <T> error(message: String) = Error<T>(message)
    }
}