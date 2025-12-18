package com.example.huertohogarfinal.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "productos")
data class Producto(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val codigo: String,
    val nombre: String,
    val precio: Int,
    val stock: Int,
    val descripcion: String,
    val categoria: String,
    val origen: String,
    val imagen: String
)