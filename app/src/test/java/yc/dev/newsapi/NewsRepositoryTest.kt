package yc.dev.newsapi

import androidx.paging.PagingConfig
import androidx.paging.PagingSource.LoadResult
import androidx.paging.testing.SnapshotLoader
import androidx.paging.testing.asSnapshot
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import yc.dev.newsapi.data.datasource.NewsDataSource
import yc.dev.newsapi.data.datasource.NewsLocalDataSource
import yc.dev.newsapi.data.datasource.NewsPagingSource
import yc.dev.newsapi.data.model.Article
import yc.dev.newsapi.data.model.remote.response.NewsErrorResponse
import yc.dev.newsapi.data.model.remote.response.NewsResponse
import yc.dev.newsapi.data.repository.NewsRepository
import yc.dev.newsapi.testutils.fake.fakeArticles
import yc.dev.newsapi.ui.state.UiState
import yc.dev.newsapi.utils.api.ApiResult
import java.net.HttpURLConnection
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals

class NewsRepositoryTest {

    @MockK
    private lateinit var mockNewsDataSource: NewsDataSource

    @MockK
    private lateinit var mockNewsLocalDataSource: NewsLocalDataSource
    private lateinit var mockNewsPagingSource: NewsPagingSource
    private lateinit var newsRepository: NewsRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this@NewsRepositoryTest)
        mockNewsPagingSource = mockk(relaxed = true)

        val testDispatcher = UnconfinedTestDispatcher()
        newsRepository = NewsRepository(
            newsDataSource = mockNewsDataSource,
            newsLocalDataSource = mockNewsLocalDataSource,
            dispatcher = testDispatcher,
        )
    }

    @Test
    fun loadNewsData_loadSuccessfulAndSaveToNewsLocalDataSource() = runTest {
        // Arrange
        val expected: UiState = UiState.Success
        val fakeResult = ApiResult.Success(
            response = NewsResponse(
                status = "ok",
                totalResults = 3,
                articles = listOf()
            )
        )
        coEvery { mockNewsDataSource.getTopHeadlines(country = any()) } returns fakeResult
        coEvery { mockNewsLocalDataSource.replaceAllArticles(articles = any()) } just Runs

        // Act
        val actual = newsRepository.loadNewsData("us").first()

        // Assert
        assertEquals(expected, actual)
        coVerify { mockNewsLocalDataSource.replaceAllArticles(any()) }
    }

    @Test
    fun loadNewsData_loadError() = runTest {
        // Arrange
        val expected: UiState = UiState.Error
        val fakeResponse = NewsErrorResponse(
            status = "error",
            code = "apiKeyInvalid",
            message = "Your API key is invalid or incorrect. Check your key, or go to https://newsapi.org to create a free API key.",
        )
        val fakeResult = ApiResult.Error(
            code = HttpURLConnection.HTTP_UNAUTHORIZED,
            errorResult = fakeResponse,
        )
        coEvery { mockNewsDataSource.getTopHeadlines(country = any()) } returns fakeResult

        // Act
        val actual = newsRepository.loadNewsData("us").first()

        // Assert
        assertEquals(expected, actual)
        coVerify(exactly = 0) { mockNewsLocalDataSource.replaceAllArticles(any()) }
    }

    /**
     * See also [NewsViewModelTest.executeGetNewsPagingDataInInitBlock_verifyArticlesStateObtainTheFirstAndTheSecondData]
     */
    @Test
    @Ignore("Try another way to mock PagingSource and inject it")
    fun getArticlesWithPageSizeAsOneAndScrollToFirstItem_obtainTheFirstAndTheSecondData() = runTest {
        // Arrange
        val expected = fakeArticles.subList(0, 2) // [a, b, c] > [a, b]

        val fakeFirstLoad = fakeArticles.subList(0, 1) // [a, b, c] > [a]
        val fakeSecondLoad = fakeArticles.subList(1, 2) // [a, b, c] > [b]
        val fakeThirdLoad = fakeArticles.subList(2, 3) // [a, b, c] > [c]
        val fakeFourthLoad = emptyList<Article>() // []
        coEvery { mockNewsPagingSource.load(any()) } returnsMany listOf(
            LoadResult.Page(fakeFirstLoad, null, 2),
            LoadResult.Page(fakeSecondLoad, 2, 3),
            LoadResult.Page(fakeThirdLoad, 3, 4),
            LoadResult.Page(fakeFourthLoad, 4, null),
        )

        // Act
        val actual = newsRepository.getNewsPagingData(pageSize = 1).asSnapshot {
            // Scroll to first item
            scrollTo(0)
        }

        // Assert
        /**
         * When pageSize = 1 and the [SnapshotLoader] scroll to index 0. The `mockNewsPagingSource.load()` should be called twice.
         *
         * `PagingConfig.initialLoadSize = pageSize` had been set in [NewsRepository.getNewsPagingData] and
         *  [PagingConfig.prefetchDistance] as [PagingConfig.pageSize] on default.
         * So the `mockNewsPagingSource.load()` will be called
         *  [PagingConfig.initialLoadSize] + [PagingConfig.prefetchDistance]
         *    = pageSize + pageSize
         *    = 1 + 1 = 2
         *  times.
         */
        coVerify(exactly = 2) { mockNewsPagingSource.load(any()) }
        assertEquals(expected, actual)
    }
}