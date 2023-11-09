package yc.dev.newsapi.data.service

import retrofit2.http.GET
import retrofit2.http.Query
import yc.dev.newsapi.BuildConfig
import yc.dev.newsapi.data.model.remote.response.NewsResponse

interface NewsService {

    @GET("v2/top-headlines")
    suspend fun getTopHeadlines(
        @Query("country") country: String,
        @Query("pageSize") pageSize: Int,
        @Query("page") page: Int,
        @Query("apiKey") apiKey: String = BuildConfig.NEWS_API_KEY,
    ): NewsResponse
}