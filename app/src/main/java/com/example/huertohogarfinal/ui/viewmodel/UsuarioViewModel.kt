package com.example.huertohogarfinal.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.huertohogarfinal.data.dao.UsuarioDao
import com.example.huertohogarfinal.data.entities.Usuario
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class UsuarioViewModel(private val dao: UsuarioDao) : ViewModel() {
    var nombre by mutableStateOf("")
    var apellido by mutableStateOf("")
    var correo by mutableStateOf("")
    var direccion by mutableStateOf("")
    var password by mutableStateOf("")

    var mensajeError by mutableStateOf<String?>(null)
    var usuarioLogueado by mutableStateOf<Usuario?>(null)

    fun validarRegistro(): Boolean {
        if (nombre.isBlank() || apellido.isBlank() || direccion.isBlank() || password.isBlank()) {
            mensajeError = "Todos los campos son obligatorios"
            return false
        }

        val regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.(com|cl)$".toRegex()
        if (!correo.matches(regex)) {
            mensajeError = "El correo debe contener '@' y terminar en .com o .cl"
            return false
        }
        return true
    }

    fun registrarCliente(onSuccess: () -> Unit) {
        if (validarRegistro()) {
            viewModelScope.launch {
                val existe = dao.buscarPorCorreo(correo)
                if (existe == null) {
                    val nuevo = Usuario(
                        nombre = nombre, apellido = apellido, correo = correo,
                        contrasena = password, direccion = direccion, rol = "CLIENTE"
                    )
                    dao.insertar(nuevo)
                    mensajeError = null
                    limpiarCampos()
                    onSuccess()
                } else {
                    mensajeError = "El correo ya está registrado."
                }
            }
        }
    }

    fun iniciarSesionCliente(onLoginSuccess: () -> Unit) {
        viewModelScope.launch {
            val usuario = dao.loginCliente(correo, password)
            if (usuario != null) {
                usuarioLogueado = usuario
                mensajeError = null
                onLoginSuccess()
            } else {
                mensajeError = "Credenciales incorrectas o cuenta no es de Cliente."
            }
        }
    }

    fun iniciarSesionEmpleado(onLoginSuccess: () -> Unit) {
        viewModelScope.launch {
            val usuario = dao.loginEmpleado(correo, password)
            if (usuario != null) {
                usuarioLogueado = usuario
                mensajeError = null
                onLoginSuccess()
            } else {
                mensajeError = "Acceso denegado. No tienes permisos de Empleado."
            }
        }
    }

    fun limpiarCampos() {
        nombre = ""; apellido = ""; correo = ""; direccion = ""; password = ""
        mensajeError = null
    }


    val listaEmpleados: StateFlow<List<Usuario>> = dao.obtenerEmpleados()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun crearEmpleado(nombre: String, apellido: String, correo: String, pass: String, direccion: String) {
        viewModelScope.launch {
            val nuevo = Usuario(
                nombre = nombre, apellido = apellido, correo = correo,
                contrasena = pass, direccion = direccion, rol = "EMPLEADO" // Siempre crea empleados
            )
            dao.insertar(nuevo)
        }
    }

    fun eliminarUsuario(usuario: Usuario) {
        viewModelScope.launch { dao.eliminar(usuario) }
    }
}