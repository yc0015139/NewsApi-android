package yc.dev.newsapi.data.datasource

import yc.dev.newsapi.data.model.remote.response.NewsResponse

class NewsDataSource {
    fun getTopHeadlines(country: String): NewsResponse {
        return NewsResponse("ok", 35)
    }

}