package com.kartikcd.mediumx.ui.profile

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.*
import com.kartikcd.api.models.entities.Update
import com.kartikcd.api.models.entities.User
import com.kartikcd.api.models.requests.UserRequest
import com.kartikcd.api.models.response.UserResponse
import com.kartikcd.mediumx.domain.MediumXRepository
import com.kartikcd.mediumx.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class ProfileViewModel(
    private val app: Application,
    private val mediumXRepository: MediumXRepository
) : AndroidViewModel(app) {

    private val _user = MutableLiveData<Resource<UserResponse>>()
    val user: LiveData<Resource<UserResponse>> = _user

    fun getCurrentUser() {
        viewModelScope.launch(Dispatchers.IO) {
            _user.postValue(Resource.Loading())
            try {
                if (isNetworkAvailable(app)) {
                    mediumXRepository.getLoggedInUser().let {
                        _user.postValue(it)
                    }
                } else {
                    _user.postValue(Resource.Error("Internet is not available."))
                }
            } catch (e: Exception) {
                _user.postValue(Resource.Error(e.message.toString()))
            }
        }
    }

    fun updateUser(update: Update) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val userRequest = UserRequest(update)
                mediumXRepository.updateUserData(userRequest).let {
                    _user.postValue(it)
                }
            } catch (e: Exception) {
                
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