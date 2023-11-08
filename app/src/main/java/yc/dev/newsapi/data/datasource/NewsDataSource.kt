package yc.dev.newsapi.data.datasource

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import yc.dev.newsapi.data.service.NewsService

class NewsDataSource(
    private val newsService: NewsService,
    private val dispatcher: CoroutineDispatcher,
) {
    fun getTopHeadlines(
        country: String,
        pageSize: Int,
        page: Int,
    ) = flow {
        val response = newsService.getTopHeadlines(country, pageSize, page)
        emit(response)
    }.flowOn(dispatcher)

}