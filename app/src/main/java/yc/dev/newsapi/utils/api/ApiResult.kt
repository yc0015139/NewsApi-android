package yc.dev.newsapi.utils.api

sealed interface ApiResult<out T> {
    data class Success<out T>(val response: T) : ApiResult<T>
    data class Error<E>(val code: Int, val errorResult: E) : ApiResult<Nothing>
}