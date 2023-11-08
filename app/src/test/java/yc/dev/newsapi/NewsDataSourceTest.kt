package yc.dev.newsapi

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class NewsDataSourceTest {

    private lateinit var newsDataSource: NewsDataSource

    @Before
    fun setup() {
        newsDataSource = NewsDataSource()
    }

    @Test
    fun callGetTopHeadlines_getResponseSuccessfully() {
        // Arrange
        val expected = NewsResponse("ok", 35)

        // Act
        val actual = newsDataSource.getTopHeadlines("us")

        // Assert
        assertEquals(expected, actual)
    }

}