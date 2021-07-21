package com.kartikcd.mediumx.ui.auth

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kartikcd.mediumx.domain.MediumXRepository

class AuthViewModelFactory(
    private val app: Application,
    private val mediumXRepository: MediumXRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AuthViewModel(
            app,
            mediumXRepository
        ) as T
    }
}