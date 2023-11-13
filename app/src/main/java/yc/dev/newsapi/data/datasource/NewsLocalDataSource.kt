package yc.dev.newsapi.data.datasource

import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import yc.dev.newsapi.data.model.Article
import yc.dev.newsapi.data.model.local.realm.RealmArticle

class NewsLocalDataSource(
    private val realmConfig: RealmConfiguration
) {

    suspend fun replaceAllArticles(articles: List<Article>) {
        val articlesRealm = articles.map { RealmArticle(it) }
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
        return emptyList()
    }
}
