package com.example.huertohogarfinal.data.api

import com.google.gson.annotations.SerializedName

data class IndicadoresResponse(
    @SerializedName("dolar") val dolar: IndicadorDetalle,
    @SerializedName("uf") val uf: IndicadorDetalle
)

data class IndicadorDetalle(
    val codigo: String,
    val nombre: String,
    val valor: Double,
    val fecha: String
)