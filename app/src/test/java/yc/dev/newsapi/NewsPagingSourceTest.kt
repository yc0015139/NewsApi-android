package yc.dev.newsapi

import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.testing.TestPager
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import yc.dev.newsapi.data.datasource.NewsLocalDataSource
import yc.dev.newsapi.data.datasource.NewsPagingSource
import yc.dev.newsapi.data.model.Article
import yc.dev.newsapi.testutils.fake.fakeArticles
import kotlin.test.Test
import kotlin.test.assertEquals

class NewsPagingSourceTest {

    private lateinit var mockNewsLocalDataSource: NewsLocalDataSource
    private lateinit var newsPagingSource: NewsPagingSource

    @Before
    fun setup() {
        mockNewsLocalDataSource = mockk()
        newsPagingSource = NewsPagingSource(mockNewsLocalDataSource)
    }

    @Test
    fun setPageSizeAndInitialLoadSizeAsTwoThenRefresh_loadTheFirstAndTheSecondData() = runTest {
        // Arrange
        val expected = fakeArticles.subList(0, 2) // [a, b, c] -> [a, b]
        val config = PagingConfig(
            pageSize = 2,
            initialLoadSize = 2,
        )
        val testPager = TestPager(config, newsPagingSource)
        val fakeReturn = fakeArticles.subList(0, 2)
        coEvery { mockNewsLocalDataSource.getArticles(page = 1, pageSize = 2) } returns fakeReturn

        // Act
        val result = testPager.run {
            refresh()
        } as PagingSource.LoadResult.Page<Int, Article>
        val actual = result.data

        // Assert
        assertEquals(expected, actual)
    }


    @Test
    fun setPageSizeAndInitialLoadSizeAsTwoThenRefreshAndAppend_loadAllOfResult() = runTest {
        // Arrange
        val expected = fakeArticles
        val config = PagingConfig(
            pageSize = 2,
            initialLoadSize = 2,
        )
        val testPager = TestPager(config, newsPagingSource)
        val fakeReturnInFirstPage = fakeArticles.subList(0, 2) // [a, b, c] -> [a, b]
        val fakeReturnInSecondPage = fakeArticles.subList(2, 3) // [a, b, c] -> [c]
        coEvery { mockNewsLocalDataSource.getArticles(page = 1, pageSize = 2) } returns fakeReturnInFirstPage
        coEvery { mockNewsLocalDataSource.getArticles(page = 2, pageSize = 2) } returns fakeReturnInSecondPage

        // Act
        val actual = testPager.run {
            val first = refresh() as PagingSource.LoadResult.Page<Int, Article>
            val second = append() as PagingSource.LoadResult.Page<Int, Article>
            first + second
        }

        // Assert
        assertEquals(expected, actual)
    }

    @Test
    fun setPageSizeAndInitialLoadSizeAsTwoThenRefreshAndAppendTwice_loadAllOfResult() = runTest {
        // Arrange
        val expected = fakeArticles
        val config = PagingConfig(
            pageSize = 2,
            initialLoadSize = 2,
        )
        val testPager = TestPager(config, newsPagingSource)
        val fakeReturnInFirstPage = fakeArticles.subList(0, 2) // [a, b, c] -> [a, b]
        val fakeReturnInSecondPage = fakeArticles.subList(2, 3) // [a, b, c] -> [c]
        val fakeReturnInEmpty = emptyList<Article>() // []
        coEvery { mockNewsLocalDataSource.getArticles(page = 1, pageSize = 2) } returns fakeReturnInFirstPage
        coEvery { mockNewsLocalDataSource.getArticles(page = 2, pageSize = 2) } returns fakeReturnInSecondPage
        coEvery { mockNewsLocalDataSource.getArticles(page = match { it >= 3 }, pageSize = 2) } returns fakeReturnInEmpty

        // Act
        val actual = testPager.run {
            val first = refresh() as PagingSource.LoadResult.Page<Int, Article>
            val second = append() as PagingSource.LoadResult.Page<Int, Article>
            val third = append() as PagingSource.LoadResult.Page<Int, Article>
            first + second + third
        }

        // Assert
        assertEquals(expected, actual)
    }

}