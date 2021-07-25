package com.kartikcd.mediumx.ui.feed

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.kartikcd.api.models.entities.Article
import com.kartikcd.mediumx.domain.MediumXRepository
import kotlinx.coroutines.flow.Flow

class PagingRepository(
    private val mediumXRepository: MediumXRepository
) {
    fun getArticleStream(): Flow<PagingData<Article>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = true
            ),
            pagingSourceFactory = { PagingDataSource(mediumXRepository) }
        ).flow
    }
}