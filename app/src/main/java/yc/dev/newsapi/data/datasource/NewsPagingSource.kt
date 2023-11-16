package yc.dev.newsapi.data.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import yc.dev.newsapi.data.model.Article
import javax.inject.Inject

class NewsPagingSource @Inject constructor(
    private val dataSource: NewsLocalDataSource,
) : PagingSource<Int, Article>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        val page = params.key ?: FIRST_PAGE
        val data = dataSource.getArticles(page, params.loadSize)
        val prevKey = if (page == FIRST_PAGE) null else page - 1
        val nextKey = if (data.isEmpty()) null else page + 1
        return LoadResult.Page(data, prevKey, nextKey)
    }

    /**
     * Just start from default key on every refresh. So set null on return.
     * @see PagingSource.getRefreshKey
     */
    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        return null
    }

    companion object {
        private const val FIRST_PAGE = 1
    }
}