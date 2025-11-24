package com.example.huertohogarfinal.data.api

import retrofit2.http.GET

interface HuertoApiService {
    @GET("/api/productos")
    suspend fun obtenerProductosBackend(): List<ProductoBackend>
}