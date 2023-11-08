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
import yc.dev.newsapi.data.model.Article
import yc.dev.newsapi.data.model.Source
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
        val fakeResponse = NewsResponse(
            status = "ok",
            totalResults = 3,
            articles = fakeArticles,
        )
        coEvery { mockNewsService.getTopHeadlines(country = any(), pageSize = any(), page = any()) } returns fakeResponse

        // Act
        val actual = newsDataSource.getTopHeadlines("us", 10, 1).first()

        // Assert
        val expected = NewsResponse(
            status = "ok",
            totalResults = 3,
            articles = fakeArticles,
        )
        assertEquals(expected, actual)
    }

    companion object {

        private val fakeArticles = listOf(
            Article(
                source = Source(id = "google-news", name = "Google News"),
                author = "t-online",
                title = "Media Markt | Dyson Akkusauger V8 zum Rekord-Tiefpreis: Unter 300 Euro - t-online",
                description = "",
                url = "https://news.google.com/rss/articles/CBMikwFodHRwczovL3d3dy50LW9ubGluZS5kZS9yYXRnZWJlci9kZWFscy9oYXVzaGFsdHNnZXJhZXRlLWRlYWxzL2lkXzEwMDA5MzY5Ni9tZWRpYS1tYXJrdC1keXNvbi1ha2t1c2F1Z2VyLXY4LXp1bS1yZWtvcmQtdGllZnByZWlzLXVudGVyLTMwMC1ldXJvLmh0bWzSAQA?oc=5",
                urlToImage = "",
                publishedAt = "2023-11-08T17:43:00Z",
                content = ""
            ),
            Article(
                source = Source(id = "google-news", name = "Google News"),
                author = "heise online",
                title = "Glasfaser-Routerzwang: Viel Kritik an Plänen der Netzbetreiber - heise online",
                description = "",
                url = "https://news.google.com/rss/articles/CBMiZWh0dHBzOi8vd3d3LmhlaXNlLmRlL25ld3MvR2xhc2Zhc2VyLVJvdXRlcnp3YW5nLVZpZWwtS3JpdGlrLWFuLVBsYWVuZW4tZGVyLU5ldHpiZXRyZWliZXItOTM1Njc5My5odG1s0gEA?oc=5",
                urlToImage = "",
                publishedAt = "2023-11-08T17:16:00Z",
                content = ""
            ),
            Article(
                source = Source(id = "google-news", name = "Google News"),
                author = "freiepresse.de",
                title = "Volkswagen in Zwickau stoppt Teil der Produktion - freiepresse.de",
                description = "",
                url = "https://news.google.com/rss/articles/CBMihgFodHRwczovL3d3dy5mcmVpZXByZXNzZS5kZS9uYWNocmljaHRlbi93aXJ0c2NoYWZ0L3dpcnRzY2hhZnQtcmVnaW9uYWwvdm9sa3N3YWdlbi1pbi16d2lja2F1LXN0b3BwdC10ZWlsLWRlci1wcm9kdWt0aW9uLWFydGlrZWwxMzEyMTAxN9IBAA?oc=5",
                urlToImage = "",
                publishedAt = "2023-11-08T16:43:27Z",
                content = ""
            )
        )
    }

}