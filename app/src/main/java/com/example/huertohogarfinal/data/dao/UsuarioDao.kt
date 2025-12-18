package com.example.huertohogarfinal.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.huertohogarfinal.data.entities.Usuario
import androidx.room.Delete
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuarioDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(usuario: Usuario)

    @Update
    suspend fun actualizar(usuario: Usuario)

    @Delete
    suspend fun eliminar(usuario: Usuario)
    @Query("SELECT * FROM usuarios WHERE correo = :correo AND contrasena = :pass AND rol = 'CLIENTE' LIMIT 1")
    suspend fun loginCliente(correo: String, pass: String): Usuario?

    @Query("SELECT * FROM usuarios WHERE correo = :correo AND contrasena = :pass AND (rol = 'EMPLEADO' OR rol = 'ADMIN') LIMIT 1")
    suspend fun loginEmpleado(correo: String, pass: String): Usuario?

    @Query("SELECT * FROM usuarios WHERE rol = 'EMPLEADO'")
    fun obtenerEmpleados(): Flow<List<Usuario>>

    @Query("SELECT * FROM usuarios WHERE correo = :correo LIMIT 1")
    suspend fun buscarPorCorreo(correo: String): Usuario?
}