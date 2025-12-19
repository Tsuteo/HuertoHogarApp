package com.example.huertohogarfinal

import com.example.huertohogarfinal.data.dao.UsuarioDao
import com.example.huertohogarfinal.data.entities.Usuario
import com.example.huertohogarfinal.ui.viewmodel.UsuarioViewModel
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class UsuarioViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val daoMock = mockk<UsuarioDao>(relaxed = true)
    private lateinit var viewModel: UsuarioViewModel

    @org.junit.Before
    fun setup() {
        viewModel = UsuarioViewModel(daoMock)
    }

    @Test
    fun `validarRegistro falla con campos vacios`() {
        viewModel.nombre = ""
        viewModel.password = ""
        val resultado = viewModel.validarRegistro()
        assertFalse(resultado)
        assertEquals("Todos los campos son obligatorios", viewModel.mensajeError)
    }

    @Test
    fun `validarRegistro falla con correo invalido`() {
        viewModel.nombre = "Test"
        viewModel.apellido = "User"
        viewModel.direccion = "Calle 1"
        viewModel.password = "123"
        viewModel.correo = "correo_malo"

        val resultado = viewModel.validarRegistro()
        assertFalse(resultado)
    }

    @Test
    fun `registrarCliente tiene exito si correo no existe`() = runTest {
        // GIVEN
        viewModel.nombre = "Juan"; viewModel.apellido = "Perez"
        viewModel.direccion = "Casa"; viewModel.password = "123"
        viewModel.correo = "juan@test.cl"

        coEvery { daoMock.buscarPorCorreo("juan@test.cl") } returns null

        viewModel.registrarCliente { }

        coVerify {
            daoMock.insertar(match { it.correo == "juan@test.cl" && it.rol == "CLIENTE" })
        }
        assertNull(viewModel.mensajeError)
    }

    @Test
    fun `registrarCliente falla si correo ya existe`() = runTest {
        viewModel.nombre = "Juan"; viewModel.apellido = "Perez"
        viewModel.direccion = "Casa"; viewModel.password = "123"
        viewModel.correo = "juan@test.cl"

        coEvery { daoMock.buscarPorCorreo("juan@test.cl") } returns Usuario(0,"","","","","","")

        viewModel.registrarCliente { }

        coVerify(exactly = 0) { daoMock.insertar(any()) } // No debió insertar nada
        assertEquals("El correo ya está registrado.", viewModel.mensajeError)
    }

    @Test
    fun `iniciarSesionCliente exitoso actualiza usuarioLogueado`() = runTest {
        viewModel.correo = "juan@test.cl"
        viewModel.password = "123"
        val usuarioFake = Usuario(1, "Juan", "P", "juan@test.cl", "123", "Dir", "CLIENTE")

        coEvery { daoMock.loginCliente("juan@test.cl", "123") } returns usuarioFake

        viewModel.iniciarSesionCliente { }

        assertEquals(usuarioFake, viewModel.usuarioLogueado)
        assertNull(viewModel.mensajeError)
    }

    @Test
    fun `iniciarSesionCliente fallido muestra error`() = runTest {
        viewModel.correo = "juan@test.cl"
        viewModel.password = "badpass"

        coEvery { daoMock.loginCliente(any(), any()) } returns null

        viewModel.iniciarSesionCliente { }

        assertNull(viewModel.usuarioLogueado)
        assertNotNull(viewModel.mensajeError)
    }
}