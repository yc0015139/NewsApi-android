package yc.dev.newsapi.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.realm.kotlin.RealmConfiguration
import yc.dev.newsapi.data.model.local.realm.RealmArticle
import yc.dev.newsapi.data.model.local.realm.RealmSource
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RealmModule {

    @Singleton
    @Provides
    fun provideRealmConfiguration(): RealmConfiguration {
        val schema = setOf(RealmArticle::class, RealmSource::class)
        return RealmConfiguration.Builder(schema)
            .build()
    }
}