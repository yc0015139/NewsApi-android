package yc.dev.newsapi.data.datasource

import io.realm.kotlin.Realm
import yc.dev.newsapi.data.model.Article

class NewsLocalDataSource(
    private val realm: Realm,
) {

    fun saveData(articles: List<Article>) {

    }
}
