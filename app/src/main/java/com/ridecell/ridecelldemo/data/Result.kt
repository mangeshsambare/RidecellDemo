package com.ridecell.ridecelldemo.data

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
 */
sealed class Result<out T : Any> {

    data class Success<out T : Any>(val data: T) : Result<T>()
    data class ErrorMessage(val message: String) : Result<String>()
    data class Error(val exception: Exception) : Result<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is ErrorMessage -> "ErrorMessage[data=$message]"
            is Error -> "Error[exception=$exception]"
        }
    }
}