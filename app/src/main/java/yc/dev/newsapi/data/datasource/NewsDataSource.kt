package yc.dev.newsapi.data.datasource

import yc.dev.newsapi.data.model.remote.response.NewsResponse
import yc.dev.newsapi.data.service.NewsService

class NewsDataSource(private val newsService: NewsService) {
    fun getTopHeadlines(country: String): NewsResponse {
        return newsService.getTopHeadlines(country)
    }

}