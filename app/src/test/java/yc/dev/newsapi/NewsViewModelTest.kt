package yc.dev.newsapi

import androidx.paging.PagingSource
import androidx.paging.testing.asSnapshot
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import yc.dev.newsapi.data.datasource.NewsDataSource
import yc.dev.newsapi.data.datasource.NewsLocalDataSource
import yc.dev.newsapi.data.datasource.NewsPagingSource
import yc.dev.newsapi.data.model.Article
import yc.dev.newsapi.data.model.remote.response.NewsResponse
import yc.dev.newsapi.data.repository.NewsRepository
import yc.dev.newsapi.testutils.fake.fakeArticles
import yc.dev.newsapi.ui.Constants
import yc.dev.newsapi.ui.ConstantsInTest
import yc.dev.newsapi.ui.state.UiState
import yc.dev.newsapi.utils.api.ApiResult
import yc.dev.newsapi.viewmodel.NewsViewModel
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class NewsViewModelTest {

    @MockK
    private lateinit var mockNewsRepository: NewsRepository
    private lateinit var newsViewModel: NewsViewModel
    private lateinit var constants: Constants
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        MockKAnnotations.init(this@NewsViewModelTest)
        Dispatchers.setMain(testDispatcher)

        // For init block
        coEvery { mockNewsRepository.getArticles(any()) } returns flow { }
        coEvery { mockNewsRepository.loadNewsData(any()) } returns flow { }

        constants = ConstantsInTest()
        newsViewModel = NewsViewModel(
            newsRepository = mockNewsRepository,
            dispatcher = testDispatcher,
            constants = constants,
        )
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    @Test
    fun initViewModel_observerArticleAndRefreshShouldBeCalled() = runTest {
        // Assert
        coVerify { mockNewsRepository.getArticles(any()) }
        coVerify { mockNewsRepository.loadNewsData(any()) }
    }

    @Test
    fun refreshItem_loadingStateGetSuccess() = runTest {
        // Arrange
        val expected = UiState.Success
        coEvery { mockNewsRepository.loadNewsData(any()) } returns flow { emit(UiState.Success) }

        // Act
        val initState = newsViewModel.loadingState.value
        newsViewModel.refresh()
        val actual = newsViewModel.loadingState.first()

        // Assert
        assertNull(initState)
        coVerify { mockNewsRepository.loadNewsData(any()) }
        assertEquals(expected, actual)
    }

    @Test
    fun refreshItem_loadingStateGetError() = runTest {
        // Arrange
        val expected = UiState.Error
        coEvery { mockNewsRepository.loadNewsData(any()) } returns flow { emit(UiState.Error) }

        // Act
        val initState = newsViewModel.loadingState.value
        newsViewModel.refresh()
        val actual = newsViewModel.loadingState.first()

        // Assert
        assertNull(initState)
        coVerify { mockNewsRepository.loadNewsData(any()) }
        assertEquals(expected, actual)
    }

    /**
     * For further explanation, see
     *  [NewsRepositoryTest.getArticlesWithPageSizeAsOneAndScrollToFirstItem_obtainTheFirstAndTheSecondData]
     */
    @Test
    fun executeGetArticlesInInitBlock_verifyArticlesStateObtainTheFirstAndTheSecondData() = runTest {
        // Arrange
        val expected = fakeArticles.subList(0, 2) // [a, b, c] > [a, b]
        mockNewsRepository = mockNewsRepositoryManually()

        // Act
        val pageSize = 1
        constants = ConstantsInTest(
            newsPageSize = pageSize
        )
        newsViewModel = NewsViewModel(
            newsRepository = mockNewsRepository,
            dispatcher = testDispatcher,
            constants = constants,
        )
        val actual = newsViewModel.articlesState.asSnapshot {
            // Scroll to first item
            scrollTo(0)
        }

        // Assert
        assertEquals(expected, actual)
    }

    private fun mockNewsRepositoryManually(): NewsRepository {
        val mockNewsDataSource = mockk<NewsDataSource>().also {
            coEvery { it.getTopHeadlines(any()) } returns ApiResult.Success(
                response = NewsResponse(
                    status = "ok",
                    totalResults = 3,
                    articles = fakeArticles
                )
            )
        }
        val mockLocalDataSource = mockk<NewsLocalDataSource>().also {
            coEvery { it.replaceAllArticles(any()) } just Runs
        }

        val mockNewsPagingSource = mockk<NewsPagingSource>(relaxed = true)
        val fakeFirstLoad = fakeArticles.subList(0, 1) // [a, b, c] > [a]
        val fakeSecondLoad = fakeArticles.subList(1, 2) // [a, b, c] > [b]
        val fakeThirdLoad = fakeArticles.subList(2, 3) // [a, b, c] > [c]
        val fakeFourthLoad = emptyList<Article>() // []
        coEvery { mockNewsPagingSource.load(any()) } returnsMany listOf(
            PagingSource.LoadResult.Page(fakeFirstLoad, null, 2),
            PagingSource.LoadResult.Page(fakeSecondLoad, 2, 3),
            PagingSource.LoadResult.Page(fakeThirdLoad, 3, 4),
            PagingSource.LoadResult.Page(fakeFourthLoad, 4, null),
        )

        return NewsRepository(
            newsDataSource = mockNewsDataSource,
            newsLocalDataSource = mockLocalDataSource,
            newsPagingSource = mockNewsPagingSource,
            dispatcher = testDispatcher,
        )
    }
}