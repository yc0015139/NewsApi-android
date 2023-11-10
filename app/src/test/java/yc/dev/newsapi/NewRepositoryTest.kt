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
import yc.dev.newsapi.data.model.remote.response.NewsResponse
import yc.dev.newsapi.data.repository.NewsRepository
import yc.dev.newsapi.ui.state.UiState
import yc.dev.newsapi.utils.api.ApiResult
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
        val fakeResponse = ApiResult.Success(
            response = NewsResponse(
                status = "ok",
                totalResults = 3,
                articles = listOf()
            )
        )
        coEvery { mockNewsDataSource.getTopHeadlines(country = any()) } returns fakeResponse
        coEvery { mockNewsLocalDataSource.saveData(articles = any()) } just Runs

        // Act
        val actual = newsRepository.loadNewsData("us").first()

        // Assert
        assertEquals(expected, actual)
        coVerify { mockNewsLocalDataSource.saveData(any()) }
    }
}