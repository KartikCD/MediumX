package com.kartikcd.mediumx.ui.profile

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kartikcd.mediumx.domain.MediumXRepository

class ProfileViewModelFactory(
    private val app: Application,
    private val mediumXRepository: MediumXRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ProfileViewModel(
            app,
            mediumXRepository
        ) as T
    }
}