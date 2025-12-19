package com.example.huertohogarfinal.data.api

import com.example.huertohogarfinal.data.entities.Usuario
import retrofit2.http.*

data class LoginRequest(val correo: String, val contrasena: String)
data class ResetPasswordRequest(val token: String, val nuevaContrasena: String)
data class ResetTokenResponse(val mensaje: String, val token: String?, val correo: String?)

interface HuertoApiService {
    @GET("/api/productos")
    suspend fun obtenerProductosBackend(): List<ProductoBackend>
    
    // Endpoints de Usuario
    @POST("/api/usuarios/registro")
    suspend fun registrarUsuario(@Body usuario: Usuario): Usuario
    
    @POST("/api/usuarios/login")
    suspend fun iniciarSesion(@Body loginRequest: LoginRequest): Usuario
    
    @POST("/api/usuarios/solicitar-reseteo")
    suspend fun solicitarReseteo(@Query("correo") correo: String): ResetTokenResponse
    
    @POST("/api/usuarios/resetear-contrasena")
    suspend fun resetearContrasena(@Body request: ResetPasswordRequest): Map<String, String>
    
    @GET("/api/usuarios/{id}")
    suspend fun obtenerUsuario(@Path("id") id: Long): Usuario
    
    @PUT("/api/usuarios/{id}")
    suspend fun actualizarUsuario(@Path("id") id: Long, @Body usuario: Usuario): Usuario
}