package yc.dev.newsapi

import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import yc.dev.newsapi.data.datasource.NewsDataSource
import yc.dev.newsapi.data.datasource.NewsLocalDataSource
import yc.dev.newsapi.data.model.remote.response.NewsErrorResponse
import yc.dev.newsapi.data.model.remote.response.NewsResponse
import yc.dev.newsapi.data.repository.NewsRepository
import yc.dev.newsapi.testutils.fake.fakeArticles
import yc.dev.newsapi.ui.state.UiState
import yc.dev.newsapi.utils.api.ApiResult
import java.net.HttpURLConnection
import kotlin.test.Test
import kotlin.test.assertEquals

class NewRepositoryTest {

    private lateinit var mockNewsDataSource: NewsDataSource
    private lateinit var mockNewsLocalDataSource: NewsLocalDataSource
    private lateinit var newsRepository: NewsRepository

    @Before
    fun setup() {
        mockNewsDataSource = mockk()
        mockNewsLocalDataSource = mockk()

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

    @Test
    fun getArticles_getFromLocalDataSourceSuccessfully() = runTest {
        // Arrange
        val expected = fakeArticles
        coEvery { mockNewsLocalDataSource.getArticles(any(), any()) } returns expected

        // Act
        val page = 1
        val pageSize = 10
        val actual = newsRepository.getArticles(page, pageSize).first()

        // Assert
        coVerify { mockNewsLocalDataSource.getArticles(page, pageSize) }
        assertEquals(expected, actual)
    }
}