package yc.dev.newsapi.data.service

import yc.dev.newsapi.BuildConfig
import yc.dev.newsapi.data.model.remote.response.NewsResponse

interface NewsService {

    suspend fun getTopHeadlines(
        country: String,
        pageSize: Int,
        page: Int,
        apiKey: String = BuildConfig.NEWS_API_KEY,
    ): NewsResponse
}