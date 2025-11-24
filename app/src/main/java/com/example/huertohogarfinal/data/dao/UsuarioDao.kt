package com.example.huertohogarfinal.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.huertohogarfinal.data.entities.Usuario

@Dao
interface UsuarioDao {
    @Insert
    suspend fun insertUsuario(usuario: Usuario)

    @Query("SELECT * FROM usuarios WHERE email = :email AND password = :pass LIMIT 1")
    suspend fun login(email: String, pass: String): Usuario?

    @Query("SELECT * FROM usuarios WHERE email = :email LIMIT 1")
    suspend fun checkEmail(email: String): Usuario?
}