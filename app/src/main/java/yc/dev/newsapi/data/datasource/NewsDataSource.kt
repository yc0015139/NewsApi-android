package yc.dev.newsapi.data.datasource

import yc.dev.newsapi.data.service.NewsService
import yc.dev.newsapi.utils.api.ApiResult

class NewsDataSource(
    private val newsService: NewsService,
) {

    suspend fun getTopHeadlines(
        country: String,
        pageSize: Int,
        page: Int,
    ): ApiResult {
        val response = newsService.getTopHeadlines(country, pageSize, page)
        val result = response.body()
        return ApiResult.Success(result)
    }

}