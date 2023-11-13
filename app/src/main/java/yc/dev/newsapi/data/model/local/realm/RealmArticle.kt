package yc.dev.newsapi.data.model.local.realm

import io.realm.kotlin.types.RealmObject
import yc.dev.newsapi.data.model.Article
import yc.dev.newsapi.data.model.Source

class RealmArticle() : RealmObject {
    var source: RealmSource? = null
        private set
    var author: String = ""
        private set
    var title: String = ""
        private set
    var description: String = ""
        private set
    var url: String = ""
        private set
    var urlToImage: String = ""
        private set
    var publishedAt: String = ""
        private set
    var content: String = ""
        private set

    constructor(article: Article): this(){
        article.let {
            source = it.source?.let { s -> RealmSource(s) }
            author = it.author
            title = it.title
            description = it.description
            url = it.url
            urlToImage = it.urlToImage
            publishedAt = it.publishedAt
            content = it.content
        }
    }

}

fun RealmArticle.toArticle(): Article = Article(
    source?.toSource(),
    author,
    title,
    description,
    url,
    urlToImage,
    publishedAt,
    content,
)

class RealmSource () : RealmObject {
    var id: String = ""
        private set
    var name: String = ""
        private set

    constructor(source: Source) : this() {
        source.let {
            id = it.id
            name = it.name
        }
    }

}

fun RealmSource.toSource() = Source(
    id = id,
    name = name,
)