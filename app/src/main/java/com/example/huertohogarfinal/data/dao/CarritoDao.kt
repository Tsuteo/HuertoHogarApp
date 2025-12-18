package com.example.huertohogarfinal.data.dao

import androidx.room.*
import com.example.huertohogarfinal.data.entities.ItemCarrito
import kotlinx.coroutines.flow.Flow

@Dao
interface CarritoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(item: ItemCarrito)

    @Update
    suspend fun actualizar(item: ItemCarrito)

    @Delete
    suspend fun eliminar(item: ItemCarrito)

    @Query("SELECT * FROM carrito")
    fun obtenerCarrito(): Flow<List<ItemCarrito>>

    @Query("SELECT * FROM carrito WHERE productoId = :productoId LIMIT 1")
    suspend fun obtenerPorProductoId(productoId: Int): ItemCarrito?

    @Query("DELETE FROM carrito")
    suspend fun vaciarCarrito()
}