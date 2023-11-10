package yc.dev.newsapi.utils.api

sealed interface ApiResult {
    data class Success<T>(val response: T) : ApiResult
    data class Error<T>(val code: Int, val errorResult: T) : ApiResult
}