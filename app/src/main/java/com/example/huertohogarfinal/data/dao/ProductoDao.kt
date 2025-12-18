package com.example.huertohogarfinal.data.dao
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.huertohogarfinal.data.entities.Producto
import kotlinx.coroutines.flow.Flow
import androidx.room.OnConflictStrategy
@Dao
interface ProductoDao {
    @Query("SELECT * FROM productos")
    fun obtenerTodos(): Flow<List<Producto>>

    @Query("SELECT * FROM productos WHERE categoria = :categoria")
    fun obtenerPorCategoria(categoria: String): Flow<List<Producto>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg productos: Producto)

    @Query("SELECT * FROM productos WHERE id = :id LIMIT 1")
    suspend fun obtenerPorId(id: Int): Producto?
}