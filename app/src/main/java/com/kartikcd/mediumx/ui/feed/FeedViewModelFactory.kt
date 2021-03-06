package com.kartikcd.mediumx.ui.feed

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kartikcd.mediumx.domain.MediumXRepository

class FeedViewModelFactory(
    private val app: Application,
    private val mediumXRepository: MediumXRepository,
    private val pagingRepository: PagingRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FeedViewModel(
            app,
            mediumXRepository,
            pagingRepository
        ) as T
    }
}