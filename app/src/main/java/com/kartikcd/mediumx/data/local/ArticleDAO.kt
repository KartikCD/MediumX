package com.kartikcd.mediumx.data.local

import androidx.room.*
import com.kartikcd.api.models.db.DBArticle
import com.kartikcd.api.models.entities.Article
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticleDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(dbArticle: DBArticle)

    @Query("SELECT * FROM articles")
    fun getAllArticles(): Flow<List<DBArticle>>

    @Delete
    suspend fun deleteArticle(dbArticle: DBArticle)
}