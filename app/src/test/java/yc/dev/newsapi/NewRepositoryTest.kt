package yc.dev.newsapi

import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import yc.dev.newsapi.data.datasource.NewsDataSource
import yc.dev.newsapi.data.datasource.NewsLocalDataSource
import yc.dev.newsapi.data.model.Article
import yc.dev.newsapi.data.model.Source
import yc.dev.newsapi.data.model.remote.response.NewsErrorResponse
import yc.dev.newsapi.data.model.remote.response.NewsResponse
import yc.dev.newsapi.data.repository.NewsRepository
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
        coEvery { mockNewsLocalDataSource.saveData(articles = any()) } just Runs

        // Act
        val actual = newsRepository.loadNewsData("us").first()

        // Assert
        assertEquals(expected, actual)
        coVerify { mockNewsLocalDataSource.saveData(any()) }
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
        coVerify(exactly = 0) { mockNewsLocalDataSource.saveData(any()) }
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

    companion object {
        private val fakeArticles = listOf(
            Article(
                source = Source(id = "cnn", name = "CNN"),
                author = "Yong Xiong, Melissa Gray, David Culver",
                title = "The National Zoo’s panda program is ending after more than 50 years as China looks elsewhere - CNN",
                description = "The three giant pandas tumble around in their enclosure at the Smithsonian National Zoo in Washington, DC, munching on bamboo shoots and leaves, climbing branches and generally looking adorable.",
                url = "https://www.cnn.com/2023/11/08/world/panda-diplomacy-us-china/index.html",
                urlToImage = "https://media.cnn.com/api/v1/images/stellar/prod/231106231740-02-panda-diplomacy-us-china.jpg?c=16x9&q=w_800,c_fill",
                publishedAt = "2023-11-08T12:20:00Z",
                content = "(CNN) The three giant pandas tumble around in their enclosure at the Smithsonian National Zoo in Washington, DC, munching on bamboo shoots and leaves, climbing branches and generally looking adorable… [+6708 chars]"
            ),
            Article(
                source = Source(name = "CNBC"),
                author = "Holly Ellyatt",
                title = "Ukraine war live updates: Russia claims Kyiv attacked 3 nuclear power plants; Putin says post-Soviet space is being weakened - CNBC",
                description = "Russian Security Council Secretary Nikolai Patrushev claimed Wednesday that Ukraine tried to attack three nuclear power plants.",
                url = "https://www.cnbc.com/2023/11/08/ukraine-war-live-updates-latest-news-on-russia-and-the-war-in-ukraine.html",
                urlToImage = "https://image.cnbcfm.com/api/v1/image/107330580-1699432376513-gettyimages-1574625072-AA_03082023_1295742.jpeg?v=1699432406&w=1920&h=1080",
                publishedAt = "2023-11-08T12:08:00Z",
                content = "Russian President Vladimir Putin said Wednesday that a number of countries are acting in a way that is \"directly aimed\" at weakening power the post-Soviet space.\r\nSpeaking in a video message to the p… [+1467 chars]"
            ),
            Article(
                source = Source(id = "cnn", name = "CNN"),
                author = "By <a href=\"/profiles/tara-subramaniam\">Tara Subramaniam</a>, Sophie Tanno, <a href=\"/profiles/adrienne-vogt\">Adrienne Vogt</a> and <a href=\"/profiles/dakin-andone\">Dakin Andone</a>, CNN",
                title = "Israel-Hamas war rages as outcry grows over Gaza crisis: Live updates - CNN",
                description = "As civilian casualties mount, Israel is facing increased international pressure over its campaign against Hamas in Gaza. \"Nothing justifies the horror being endured by civilians,\" a WHO spokesperson said. Follow for live updates.",
                url = "https://www.cnn.com/middleeast/live-news/israel-hamas-war-gaza-news-11-08-23/index.html",
                urlToImage = "https://cdn.cnn.com/cnnnext/dam/assets/231107133010-02-israel-gaza-gallery-update-110723-super-tease.jpeg",
                publishedAt = "2023-11-08T12:00:00Z",
                content = "Group of Seven (G7) foreign ministers on Wednesday voiced support for humanitarian pauses in Gaza to support aid deliveries, civilian movement and the release of hostages but stopped short of calling… [+1638 chars]"
            )
        )
    }
}