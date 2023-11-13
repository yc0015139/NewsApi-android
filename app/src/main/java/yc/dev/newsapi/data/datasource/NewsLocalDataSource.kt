package yc.dev.newsapi.data.datasource

import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query
import yc.dev.newsapi.data.model.Article
import yc.dev.newsapi.data.model.local.realm.RealmArticle
import yc.dev.newsapi.data.model.local.realm.toArticle
import kotlin.math.max

class NewsLocalDataSource(
    private val realmConfig: RealmConfiguration
) {

    suspend fun replaceAllArticles(articles: List<Article>) {
        val articlesRealm = articles.mapIndexed { idx, it -> RealmArticle(idx, it) }
        val realm = Realm.open(realmConfig)
        realm.write {
            deleteAll()
            articlesRealm.forEach {
                copyToRealm(it)
            }
        }
        realm.close()
    }

    fun getArticles(page: Int, pageSize: Int): List<Article> {
        if (pageSize <= 0) return emptyList()

        val start = max(0, (page - 1) * pageSize)
        val end = start + pageSize
        val pagedArticles = Realm.open(realmConfig).let { realm ->
            val query = "index >= $0 AND index < $1"
            val articles = realm.query<RealmArticle>(query, start, end)
                .find()
                .map { it.toArticle() }
            realm.close()
            articles
        }
        return pagedArticles
    }
}
