package yc.dev.newsapi.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import yc.dev.newsapi.data.datasource.NewsDataSource
import yc.dev.newsapi.data.datasource.NewsLocalDataSource
import yc.dev.newsapi.data.datasource.NewsPagingSource
import yc.dev.newsapi.data.model.Article
import yc.dev.newsapi.data.model.remote.response.NewsResponse
import yc.dev.newsapi.ui.state.UiState
import yc.dev.newsapi.utils.api.ApiResult
import javax.inject.Inject

class NewsRepository @Inject constructor(
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

    // TODO: Implement the RemoteMediator for Pager
    fun getNewsPagingData(pageSize: Int): Flow<PagingData<Article>> {
        val pagingConfig = PagingConfig(
            pageSize = pageSize,
            initialLoadSize = pageSize,
        )
        return Pager(config = pagingConfig) { NewsPagingSource(newsLocalDataSource) }
            .flow
    }
}