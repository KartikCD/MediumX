package com.kartikcd.api

import com.kartikcd.api.services.MediumXAuthApiService
import com.kartikcd.api.services.MediumXPublicApiService
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit



object MediumXClient {

    var authToken: String? = null

    private val authInterceptor = Interceptor {
        var req = it.request()
        authToken?.let {
            req = req.newBuilder()
                .header("Authorization", "Token $it")
                .build()
        }
        it.proceed(req)
    }

    val okhttpBuilder = OkHttpClient.Builder()
        .readTimeout(30, TimeUnit.SECONDS)
        .connectTimeout(5, TimeUnit.SECONDS)

    val retrofitBuilder = Retrofit.Builder().baseUrl("https://conduit.productionready.io/api/")
        .addConverterFactory(GsonConverterFactory.create())

    val PUBLIC_API:MediumXPublicApiService = retrofitBuilder.client(okhttpBuilder.build()).build().create(MediumXPublicApiService::class.java)

    val authApi: MediumXAuthApiService = retrofitBuilder
        .client(okhttpBuilder.addInterceptor(authInterceptor).build())
        .build()
        .create(MediumXAuthApiService::class.java)
}


