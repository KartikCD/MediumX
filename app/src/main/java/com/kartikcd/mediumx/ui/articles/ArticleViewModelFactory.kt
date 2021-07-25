package com.kartikcd.mediumx.ui.articles

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kartikcd.mediumx.domain.MediumXRepository

class ArticleViewModelFactory(
    private val app: Application,
    private val mediumXRepository: MediumXRepository
): ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ArticleViewModel(
            app,
            mediumXRepository
        ) as T
    }
}