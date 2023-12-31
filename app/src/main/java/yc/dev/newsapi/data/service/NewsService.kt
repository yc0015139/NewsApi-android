package yc.dev.newsapi.data.service

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import yc.dev.newsapi.data.model.remote.response.NewsResponse

interface NewsService {

    @GET("v2/top-headlines")
    suspend fun getTopHeadlines(
        @Query("country") country: String,
        @Query("apiKey") apiKey: String,
    ): Response<NewsResponse>
}