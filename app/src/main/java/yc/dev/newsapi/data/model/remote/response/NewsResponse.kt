package yc.dev.newsapi.data.model.remote.response

import yc.dev.newsapi.data.model.Article

data class NewsResponse(
    val status: String,
    val totalResults: Int,
    val articles: List<Article>,
)
