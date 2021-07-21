package com.kartikcd.mediumx.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kartikcd.api.models.db.DBArticle
import com.kartikcd.api.models.entities.Article

@Database(
    entities = [DBArticle::class],
    version = 1,
    exportSchema = false
)
abstract class ArticleDatabase: RoomDatabase() {
    abstract fun getArticleDAO(): ArticleDAO
}