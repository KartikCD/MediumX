package com.kartikcd.mediumx.data.local

import android.app.Application
import androidx.room.Room
import kotlin.coroutines.coroutineContext

class MediumXLocalClient {
    fun getDAO(app: Application): ArticleDAO {
        val databaseBuilder = Room.databaseBuilder(app, ArticleDatabase::class.java, "medium_db")
            .fallbackToDestructiveMigration()
            .build()

        return databaseBuilder.getArticleDAO()
    }
}