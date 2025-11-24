package com.example.huertohogarfinal.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "carrito")
data class ItemCarrito(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombreProducto: String,
    val precio: Int,
    val cantidad: Int = 1,
    val estado: String = "PENDIENTE"
)