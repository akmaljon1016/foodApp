package com.example.foodapp

fun main() {
    val networkAvailable = true
    var result: NetworkResult<Int>? = null
    if (networkAvailable) {
        result = handleNetworkResult("404")
    } else {
        result = NetworkResult.Error("Networ not available")
    }
    when (result) {
     is NetworkResult.Seccess->{
         println(result.data)
     }
        is NetworkResult.Error->{
            println(result.message)
            println(result.data)
        }
    }

}

fun handleNetworkResult(result: String): NetworkResult<Int> {
    when (result) {
        "404" -> {
            return NetworkResult.Error("Errror", 12)
        }
        "Success" -> {
            return NetworkResult.Seccess(12)
        }
        else -> {
            return NetworkResult.Loading()
        }
    }

}

sealed class NetworkResult<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Seccess<T>(data: T?) : NetworkResult<T>(data)
    class Error<T>(message: String?, data: T? = null) : NetworkResult<T>(data, message)
    class Loading<T> : NetworkResult<T>()
}