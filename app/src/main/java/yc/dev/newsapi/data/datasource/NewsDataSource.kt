package yc.dev.newsapi.data.datasource

import kotlinx.serialization.json.Json
import yc.dev.newsapi.BuildConfig
import yc.dev.newsapi.data.model.remote.response.NewsErrorResponse
import yc.dev.newsapi.data.model.remote.response.NewsResponse
import yc.dev.newsapi.data.service.NewsService
import yc.dev.newsapi.utils.api.ApiResult
import javax.inject.Inject

class NewsDataSource @Inject constructor(
    private val newsService: NewsService,
) {

    suspend fun getTopHeadlines(
        country: String,
        apiKey: String = BuildConfig.NEWS_API_KEY,
    ): ApiResult<NewsResponse> {

        val response = try {
            newsService.getTopHeadlines(country, apiKey)
        } catch (e: Exception) {
            return ApiResult.Error(-1, e.toString())
        }
        if (response.isSuccessful) {
            val result = response.body()
                ?: throw IllegalStateException("Response is successful but response body is null.")
            return ApiResult.Success(result)
        }

        val errorResponse = response.errorBody()?.let {
            Json.decodeFromString<NewsErrorResponse>(it.string())
        } ?: throw IllegalStateException("Error on decode the error body")
        return ApiResult.Error(response.code(), errorResponse)
    }

}