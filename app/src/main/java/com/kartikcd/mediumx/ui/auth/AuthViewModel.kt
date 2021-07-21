package com.kartikcd.mediumx.ui.auth

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.*
import com.kartikcd.api.models.entities.SignupData
import com.kartikcd.api.models.requests.SignupRequest
import com.kartikcd.api.models.response.UserResponse
import com.kartikcd.mediumx.domain.MediumXRepository
import com.kartikcd.mediumx.util.Resource
import kotlinx.coroutines.launch
import java.lang.Exception

class AuthViewModel(
    private val app: Application,
    private val mediumXRepository: MediumXRepository
) : AndroidViewModel(app) {

    private val _user = MutableLiveData<Resource<UserResponse>>()
    val user: LiveData<Resource<UserResponse>> = _user

    fun signup(email: String, password: String, username: String) {
        viewModelScope.launch {
            _user.postValue(Resource.Loading())
            try {
                if (isNetworkAvailable(app)) {
                    val userCreds = SignupData(email, password, username)
                    mediumXRepository.registerUser(SignupRequest(userCreds)).let {
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