package yc.dev.newsapi

import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import yc.dev.newsapi.data.repository.NewsRepository
import yc.dev.newsapi.ui.state.UiState
import yc.dev.newsapi.viewmodel.NewsViewModel
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class NewsViewModelTest {

    @MockK
    private lateinit var mockNewsRepository: NewsRepository
    private lateinit var newsViewModel: NewsViewModel
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(testDispatcher)

        // For init block
        coEvery { mockNewsRepository.getArticles(any()) } returns flow { }
        coEvery { mockNewsRepository.loadNewsData(any()) } returns flow { }

        newsViewModel = NewsViewModel(
            newsRepository = mockNewsRepository,
            dispatcher = testDispatcher,
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
    
}