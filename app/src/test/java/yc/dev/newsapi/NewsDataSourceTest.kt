package yc.dev.newsapi

import io.mockk.every
import io.mockk.mockk
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
        newsDataSource = NewsDataSource(mockNewsService)
    }

    @Test
    fun callGetTopHeadlines_getResponseSuccessfully() {
        // Arranger
        val fakeResponse = NewsResponse(status = "ok", totalResults = 35)
        every { mockNewsService.getTopHeadlines("us") } returns fakeResponse

        // Act
        val actual = newsDataSource.getTopHeadlines("us")

        // Assert
        val expected = NewsResponse(status = "ok", totalResults = 35)
        assertEquals(expected, actual)
    }

}