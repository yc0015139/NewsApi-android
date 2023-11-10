package yc.dev.newsapi

import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import yc.dev.newsapi.data.datasource.NewsDataSource
import yc.dev.newsapi.data.model.remote.response.NewsErrorResponse
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
        val fakeResponse = NewsResponse(
            status = "ok",
            totalResults = 36,
            articles = listOf(),
        )
        val expected = ApiResult.Success(
            response = fakeResponse
        ).response
        mockWebServer.enqueueResponse("api_get_top_head_lines_success.json", HttpURLConnection.HTTP_OK)

        // Act
        val response = newsDataSource.getTopHeadlines("us")

        // Assert
        val actual = assertIs<ApiResult.Success<NewsResponse>>(response).response
        assertEquals(expected.status, actual.status)
        assertEquals(expected.totalResults, actual.totalResults)
    }

    @Test
    fun callGetTopHeadlines_getResponseErrorWithApiKeyInvalid() = runTest {
        // Arrange
        val fakeResponse = NewsErrorResponse(
            status = "error",
            code = "apiKeyInvalid",
            message = "Your API key is invalid or incorrect. Check your key, or go to https://newsapi.org to create a free API key.",
        )
        val expectedApiResult = ApiResult.Error(
            code = HttpURLConnection.HTTP_UNAUTHORIZED,
            errorResult = fakeResponse,
        )
        val expected = expectedApiResult.errorResult
        mockWebServer.enqueueResponse(
            "api_get_top_head_lines_error_api_key_invalid.json",
            HttpURLConnection.HTTP_UNAUTHORIZED,
        )

        // Act
        val response = newsDataSource.getTopHeadlines("us", apiKey = "")

        // Assert
        val apiResult = assertIs<ApiResult.Error<NewsErrorResponse>>(response)
        assertEquals(expectedApiResult.code, apiResult.code)
        val actual = apiResult.errorResult
        assertEquals(expected, actual)
    }
}