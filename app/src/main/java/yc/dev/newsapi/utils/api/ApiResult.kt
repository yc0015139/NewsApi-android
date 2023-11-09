package yc.dev.newsapi.utils.api

sealed interface ApiResult {
    data class Success<T>(val result: T) : ApiResult
}