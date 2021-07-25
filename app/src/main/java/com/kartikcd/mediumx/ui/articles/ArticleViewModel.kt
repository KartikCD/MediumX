package com.kartikcd.mediumx.ui.articles

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kartikcd.api.models.requests.ArticleRequest
import com.kartikcd.api.models.response.ArticleResponse
import com.kartikcd.mediumx.domain.MediumXRepository
import com.kartikcd.mediumx.util.Resource
import kotlinx.coroutines.launch
import java.lang.Exception

class ArticleViewModel(
    private val app: Application,
    private val mediumXRepository: MediumXRepository
): AndroidViewModel(app) {

    private val _article = MutableLiveData<Resource<ArticleResponse>>()
    val article: LiveData<Resource<ArticleResponse>> = _article

    fun addArticle(articleRequest: ArticleRequest) {
        viewModelScope.launch {
            _article.postValue(Resource.Loading())
            try {
                if(isNetworkAvailable(app)) {
                    _article.postValue(mediumXRepository.createArticle(articleRequest))
                } else {
                    _article.postValue(Resource.Error("Internet is not available."))
                }
            } catch (e: Exception) {
                _article.postValue(Resource.Error(e.message.toString()))
            }
        }
    }

    private fun isNetworkAvailable(context: Context?):Boolean{
        if (context == null) return false
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                        return true
                    }
                }
            }
        } else {
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                return true
            }
        }
        return false
    }
}