package yc.dev.newsapi.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import yc.dev.newsapi.data.datasource.NewsDataSource
import yc.dev.newsapi.data.datasource.NewsLocalDataSource
import yc.dev.newsapi.data.model.remote.response.NewsResponse
import yc.dev.newsapi.ui.state.UiState
import yc.dev.newsapi.utils.api.ApiResult

class NewsRepository(
    private val newsDataSource: NewsDataSource,
    private val newsLocalDataSource: NewsLocalDataSource,
    private val dispatcher: CoroutineDispatcher,
) {
    suspend fun loadNewsData(country: String) = flow {
        when (val result = newsDataSource.getTopHeadlines(country)) {
            is ApiResult.Success<NewsResponse> -> {
                val articles = result.response.articles
                newsLocalDataSource.replaceAllArticles(articles)
                emit(UiState.Success)
            }

            is ApiResult.Error<*> -> emit(UiState.Error)
        }
    }.flowOn(dispatcher)

    suspend fun getArticles(page: Int, pageSize: Int) = flow {
        val articles = newsLocalDataSource.getArticles(page, pageSize)
        emit(articles)
    }.flowOn(dispatcher)
}