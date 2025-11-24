package com.example.huertohogarfinal.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.huertohogarfinal.data.api.RetrofitClient
import com.example.huertohogarfinal.data.dao.CarritoDao
import com.example.huertohogarfinal.data.dao.ProductoDao
import com.example.huertohogarfinal.data.dao.UsuarioDao
import com.example.huertohogarfinal.data.entities.ItemCarrito
import com.example.huertohogarfinal.data.entities.Producto
import com.example.huertohogarfinal.data.entities.Usuario
import kotlinx.coroutines.launch
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager

class UsuarioViewModel(private val usuarioDao: UsuarioDao) : ViewModel() {

    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var nombre by mutableStateOf("")

    var isError by mutableStateOf(false)
    var mensajeError by mutableStateOf("")
    var loginExitoso by mutableStateOf(false)

    fun login() {
        viewModelScope.launch {
            if (email.isBlank() || password.isBlank()) {
                isError = true
                mensajeError = "Por favor completa todos los campos"
                return@launch
            }

            if (password.length < 6) {
                isError = true
                mensajeError = "La contraseña debe tener al menos 6 caracteres"
                return@launch
            }

            val usuario = usuarioDao.login(email, password)

            if (usuario != null) {
                loginExitoso = true
                isError = false
            } else {
                isError = true
                mensajeError = "Credenciales incorrectas"
            }
        }
    }

    fun registrarUsuarioPrueba() {
        viewModelScope.launch {
            val nuevoUsuario = Usuario(
                nombre = "Juan", apellido = "Perez",
                email = "juan@duoc.cl", password = "123456",
                direccion = "Calle Falsa 123", telefono = "99999999"
            )
            usuarioDao.insertUsuario(nuevoUsuario)
            mensajeError = "Usuario de prueba creado"
            isError = true
        }
    }
}

class UsuarioViewModelFactory(private val usuarioDao: UsuarioDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UsuarioViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UsuarioViewModel(usuarioDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}