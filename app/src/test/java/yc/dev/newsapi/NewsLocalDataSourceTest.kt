package yc.dev.newsapi

import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query
import kotlinx.coroutines.test.runTest
import org.junit.Before
import yc.dev.newsapi.data.datasource.NewsLocalDataSource
import yc.dev.newsapi.data.model.local.realm.RealmArticle
import yc.dev.newsapi.data.model.local.realm.RealmSource
import yc.dev.newsapi.data.model.local.realm.toArticle
import yc.dev.newsapi.testutils.fake.fakeArticles
import kotlin.test.Test
import kotlin.test.assertEquals

class NewsLocalDataSourceTest {

    private lateinit var realmConfig: RealmConfiguration
    private lateinit var newsLocalDataSource: NewsLocalDataSource

    @Before
    fun setUp() {
        val schema = setOf(RealmArticle::class, RealmSource::class)
        realmConfig = RealmConfiguration.Builder(schema)
            .build()
        newsLocalDataSource = NewsLocalDataSource(realmConfig)
    }

    @Test
    fun replaceAllArticle_verifyTheStoredData() = runTest {
        // Arrange
        val expected = fakeArticles

        // Act
        newsLocalDataSource.replaceAllArticles(expected)

        // Assert
        val realm = Realm.open(realmConfig)
        val actual = realm.query<RealmArticle>().find().map { it.toArticle() }
        realm.close()
        assertEquals(expected, actual)
    }

    @Test
    fun getArticlesInFirstPageAndThePageSizeAsTwo_getTheFirstAndTheSecondOfData() = runTest {
        // Arrange
        val expected = fakeArticles.subList(0, 2) // [a, b, c] -> [a, b]

        // Act
        newsLocalDataSource.replaceAllArticles(fakeArticles)
        val actual = newsLocalDataSource.getArticles(page = 1, pageSize = 2)

        // Assert
        assertEquals(expected, actual)
    }

    @Test
    fun getArticlesInSecondPageAndThePageSizeAsTwo_getTheThirdOfData() = runTest {
        // Arrange
        val expected = fakeArticles.subList(2, 3) // [a, b, c] -> [c]

        // Act
        newsLocalDataSource.replaceAllArticles(fakeArticles)
        val actual = newsLocalDataSource.getArticles(page = 2, pageSize = 2)

        // Assert
        assertEquals(expected, actual)
    }

    @Test
    fun getArticlesInFirstPageAndThePageSizeAsThree_getAllOfData() = runTest {
        // Arrange
        val expected = fakeArticles.subList(0, 3) // [a, b, c] -> [a, b, c]

        // Act
        newsLocalDataSource.replaceAllArticles(fakeArticles)
        val actual = newsLocalDataSource.getArticles(page = 1, pageSize = 3)

        // Assert
        assertEquals(expected, actual)
    }

    @Test
    fun getArticlesInSecondPageAndThePageSizeAsThree_getEmpty() = runTest {
        // Arrange
        val expected = fakeArticles.subList(3, 3) // [a, b, c] -> []

        // Act
        newsLocalDataSource.replaceAllArticles(fakeArticles)
        val actual = newsLocalDataSource.getArticles(page = 2, pageSize = 3)

        // Assert
        assertEquals(expected, actual)
    }

    @Test
    fun getArticlesInSecondPageAndThePageSizeAsOne_getTheSecondOfData() = runTest {
        // Arrange
        val expected = fakeArticles.subList(1, 2) // [a, b, c] -> [b]

        // Act
        newsLocalDataSource.replaceAllArticles(fakeArticles)
        val actual = newsLocalDataSource.getArticles(page = 2, pageSize = 1)

        // Assert
        assertEquals(expected, actual)
    }

}