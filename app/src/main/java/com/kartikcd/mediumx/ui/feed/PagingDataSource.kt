package com.kartikcd.mediumx.ui.feed

import androidx.paging.PagingSource
import com.kartikcd.api.models.entities.Article
import com.kartikcd.mediumx.domain.MediumXRepository
import java.lang.Exception

class PagingDataSource(
    private val mediumXRepository: MediumXRepository
): PagingSource<Int, Article>() {

    private var START_OFFSET: Int = 0

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        val position = params.key ?: START_OFFSET

        return try {
            val response = mediumXRepository.getArticles(params.loadSize, position)

            response.data?.let {
                val nextKey = if(it.articles.isEmpty()) {
                    null
                } else {
                    position + 20
                }
                LoadResult.Page(
                    data = it.articles,
                    prevKey = if (position == START_OFFSET) null else position - 20,
                    nextKey = nextKey
                )
            }!!
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }
}

