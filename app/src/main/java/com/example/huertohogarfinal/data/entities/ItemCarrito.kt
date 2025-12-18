package com.example.huertohogarfinal.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "carrito")
data class ItemCarrito(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val productoId: Int,
    val nombreProducto: String,
    val precioUnitario: Int,
    val cantidad: Int,
    val total: Int
)