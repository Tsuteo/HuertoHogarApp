package com.example.huertohogarfinal.data.dao
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.huertohogarfinal.data.entities.Producto

@Dao
interface ProductoDao {
    @Insert
    suspend fun insertAll(vararg productos: Producto)

    @Query("SELECT * FROM productos")
    suspend fun getAllProductos(): List<Producto>

    @Query("SELECT * FROM productos WHERE categoria = :categoria")
    suspend fun getProductosPorCategoria(categoria: String): List<Producto>
}