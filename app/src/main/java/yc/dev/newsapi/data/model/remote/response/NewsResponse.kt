package yc.dev.newsapi.data.model.remote.response

import kotlinx.serialization.Serializable
import yc.dev.newsapi.data.model.Article

@Serializable
data class NewsResponse(
    val status: String,
    val totalResults: Int,
    val articles: List<Article>,
)

@Serializable
data class NewsErrorResponse(
    val status: String,
    val code: String? = null,
    val message: String = "",
)
