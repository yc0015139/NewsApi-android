package yc.dev.newsapi

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import yc.dev.newsapi.data.datasource.NewsDataSource
import yc.dev.newsapi.data.model.remote.response.NewsResponse
import yc.dev.newsapi.data.service.NewsService
import yc.dev.newsapi.testutils.enqueueResponse
import yc.dev.newsapi.testutils.service
import java.net.HttpURLConnection

class NewsDataSourceTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var mockNewsService: NewsService
    private lateinit var newsDataSource: NewsDataSource

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        mockNewsService = mockWebServer.service(NewsService::class.java)

        val testDispatcher = UnconfinedTestDispatcher()
        newsDataSource = NewsDataSource(mockNewsService, testDispatcher)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun callGetTopHeadlines_getResponseSuccessfully() = runTest {
        // Arrange
        mockWebServer.enqueueResponse("api_get_top_head_lines_success.json", HttpURLConnection.HTTP_OK)

        // Act
        val actual = newsDataSource.getTopHeadlines("us", 10, 1).first()

        // Assert
        val expected = NewsResponse(
            status = "ok",
            totalResults = 36,
            articles = listOf(),
        )
        assertEquals(expected.status, actual.status)
        assertEquals(expected.totalResults, actual.totalResults)
    }
}