package com.example.huertohogarfinal.data.api

data class ProductoBackend(
    val id: Long,
    val nombre: String,
    val precio: Int,
    val stock: Int,
    val categoria: String
)