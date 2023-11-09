package yc.dev.newsapi

import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import yc.dev.newsapi.data.datasource.NewsDataSource
import yc.dev.newsapi.data.model.remote.response.NewsResponse
import yc.dev.newsapi.data.service.NewsService
import yc.dev.newsapi.testutils.enqueueResponse
import yc.dev.newsapi.testutils.service
import yc.dev.newsapi.utils.api.ApiResult
import java.net.HttpURLConnection
import kotlin.test.assertEquals
import kotlin.test.assertIs

class NewsDataSourceTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var mockNewsService: NewsService
    private lateinit var newsDataSource: NewsDataSource

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        mockNewsService = mockWebServer.service(NewsService::class.java)
        newsDataSource = NewsDataSource(mockNewsService)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun callGetTopHeadlines_getResponseSuccessfully() = runTest {
        // Arrange
        val expectedResult = NewsResponse(
            status = "ok",
            totalResults = 36,
            articles = listOf(),
        )
        val expected = ApiResult.Success(
            result = expectedResult
        ).result
        mockWebServer.enqueueResponse("api_get_top_head_lines_success.json", HttpURLConnection.HTTP_OK)

        // Act
        val actualResult = newsDataSource.getTopHeadlines("us", 10, 1)

        // Assert
        val actual = assertIs<ApiResult.Success<NewsResponse>>(actualResult).result
        assertEquals(expected.status, actual.status)
        assertEquals(expected.totalResults, actual.totalResults)
    }
}