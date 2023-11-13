package yc.dev.newsapi.data.datasource

import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query
import yc.dev.newsapi.data.model.Article
import yc.dev.newsapi.data.model.local.realm.RealmArticle
import yc.dev.newsapi.data.model.local.realm.toArticle

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
        val realm = Realm.open(realmConfig)
        val start = (page - 1) * pageSize
        val end = start + pageSize

        val query = "index >= $0 AND index < $1"
        val pagedArticles = realm.query<RealmArticle>(query, start, end)
            .find()
            .map { it.toArticle() }
        realm.close()

        return pagedArticles
    }
}
