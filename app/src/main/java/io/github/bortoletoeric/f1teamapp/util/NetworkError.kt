package io.github.bortoletoeric.f1teamapp.util

import retrofit2.HttpException
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

sealed class NetworkError {
    object NoInternet : NetworkError()
    object Timeout : NetworkError()
    object ServerError : NetworkError()
    data class Unknown(val message: String?) : NetworkError()

    fun toMessage(): String {
        return when (this) {
            is NoInternet -> "Parece que você está sem internet. Verifique sua conexão."
            is Timeout -> "A conexão demorou muito. Tente novamente."
            is ServerError -> "O servidor está passando por instabilidades. Tente mais tarde."
            is Unknown -> message ?: "Ocorreu um erro inesperado."
        }
    }
}

fun Throwable.toNetworkError(): NetworkError {
    return when (this) {
        is UnknownHostException, is ConnectException -> NetworkError.NoInternet
        is SocketTimeoutException -> NetworkError.Timeout
        is HttpException -> {
            if (this.code() in 500..599) NetworkError.ServerError
            else NetworkError.Unknown("Erro do servidor: ${this.code()}")
        }
        is IOException -> NetworkError.Unknown("Erro de rede: ${this.message}")
        else -> NetworkError.Unknown(this.message)
    }
}
