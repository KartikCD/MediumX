package com.kartikcd.mediumx.ui.feed

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.kartikcd.api.models.db.DBArticle
import com.kartikcd.api.models.entities.Article
import com.kartikcd.api.models.response.ArticlesResponse
import com.kartikcd.mediumx.data.local.ArticleDAO
import com.kartikcd.mediumx.domain.MediumXRepository
import com.kartikcd.mediumx.util.Resource
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.lang.Exception

class FeedViewModel(
    private val app: Application,
    private val mediumXRepository: MediumXRepository,
    private val pagingRepository: PagingRepository
) : AndroidViewModel(app) {

    private val _globalFeed = MutableLiveData<Resource<ArticlesResponse>>()
    val globalFeed: LiveData<Resource<ArticlesResponse>> = _globalFeed

    private val _localFeed = MutableLiveData<List<DBArticle>>()
    val localFeed: LiveData<List<DBArticle>> = _localFeed

    private var globalFeedResult: Flow<PagingData<Article>>? = null

    // Fetching global feed from network
    fun fetchGlobalFeed() {
        viewModelScope.launch {
            _globalFeed.postValue(Resource.Loading())
            try {
                if (isNetworkAvailable(app)) {
                    val articleResult = mediumXRepository.getArticles()
                    _globalFeed.postValue(articleResult)
                } else {
                    _globalFeed.postValue(Resource.Error("Internet is not available."))
                }
            } catch (e: Exception) {
                _globalFeed.postValue(Resource.Error(e.message.toString()))
            }
        }
    }

    fun getArticleList(): Flow<PagingData<Article>> {
        val lastResult = globalFeedResult
        if (lastResult != null) {
            return lastResult
        }

        val newResult: Flow<PagingData<Article>> = pagingRepository.getArticleStream().cachedIn(viewModelScope)
        globalFeedResult = newResult
        return newResult
    }

    //Saving article to local database
    fun saveArticle(dbArticle: DBArticle, articleDAO: ArticleDAO) {
        viewModelScope.launch {
            mediumXRepository.saveArticlesToLocalDatabase(dbArticle, articleDAO)
        }
    }

    //fetching saved article from local database
    fun fetchLocalFeed(articleDAO: ArticleDAO) =
        liveData {
            mediumXRepository.getSavedArticle(articleDAO).collect {
                emit(it)
            }
        }

    fun deleteSaveArticle(dbArticle: DBArticle, articleDAO: ArticleDAO) {
        viewModelScope.launch {
            mediumXRepository.deleteSavedArticle(dbArticle, articleDAO)
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