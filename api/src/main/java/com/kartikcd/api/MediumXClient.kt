package com.kartikcd.api

import com.kartikcd.api.services.MediumXService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit



object MediumXClient {

    val okhttpBuilder = OkHttpClient.Builder()
        .readTimeout(5, TimeUnit.SECONDS)
        .connectTimeout(2, TimeUnit.SECONDS)

    val retrofitBuilder = Retrofit.Builder().baseUrl("https://conduit.productionready.io/api/")
        .addConverterFactory(GsonConverterFactory.create())

    val publicApi:MediumXService = retrofitBuilder.client(okhttpBuilder.build()).build().create(MediumXService::class.java)
}


