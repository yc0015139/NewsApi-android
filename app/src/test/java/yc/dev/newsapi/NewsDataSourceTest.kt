package yc.dev.newsapi

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import yc.dev.newsapi.data.datasource.NewsDataSource
import yc.dev.newsapi.data.model.remote.response.NewsResponse
import yc.dev.newsapi.data.service.NewsService

class NewsDataSourceTest {

    private lateinit var mockNewsService: NewsService
    private lateinit var newsDataSource: NewsDataSource

    @Before
    fun setup() {
        mockNewsService = mockk()

        val testDispatcher = UnconfinedTestDispatcher()
        newsDataSource = NewsDataSource(mockNewsService, testDispatcher)
    }

    @Test
    fun callGetTopHeadlines_getResponseSuccessfully() = runTest {
        // Arrange
        val fakeResponse = NewsResponse(status = "ok", totalResults = 35)
        coEvery { mockNewsService.getTopHeadlines("us") } returns fakeResponse

        // Act
        val actual = newsDataSource.getTopHeadlines("us").first()

        // Assert
        val expected = NewsResponse(status = "ok", totalResults = 35)
        assertEquals(expected, actual)
    }

}