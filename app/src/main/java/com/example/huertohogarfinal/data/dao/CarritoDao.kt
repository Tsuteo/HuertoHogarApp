package com.example.huertohogarfinal.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.huertohogarfinal.data.entities.ItemCarrito

@Dao
interface CarritoDao {
    @Insert
    suspend fun insertar(item: ItemCarrito)

    @Query("SELECT * FROM carrito WHERE estado = 'PENDIENTE'")
    suspend fun obtenerCarrito(): List<ItemCarrito>

    @Query("SELECT SUM(precio * cantidad) FROM carrito WHERE estado = 'PENDIENTE'")
    suspend fun obtenerTotal(): Int?

    @Query("DELETE FROM carrito")
    suspend fun vaciarCarrito()
}