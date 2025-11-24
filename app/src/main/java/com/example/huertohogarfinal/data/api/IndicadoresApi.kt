package com.example.huertohogarfinal.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface IndicadoresService {
    @GET("api")
    suspend fun obtenerIndicadores(): IndicadoresResponse
}

object RetrofitClient {
    private const val BASE_URL = "https://mindicador.cl/"

    val instance: IndicadoresService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(IndicadoresService::class.java)
    }
}