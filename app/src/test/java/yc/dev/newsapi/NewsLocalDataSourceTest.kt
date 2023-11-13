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
}