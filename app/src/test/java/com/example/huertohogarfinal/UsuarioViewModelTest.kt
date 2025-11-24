package com.example.huertohogarfinal

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.huertohogarfinal.data.dao.UsuarioDao
import com.example.huertohogarfinal.ui.viewmodel.UsuarioViewModel
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class UsuarioViewModelTest {

    // Regla para que LiveData/State funcione en tests
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: UsuarioViewModel
    private lateinit var usuarioDao: UsuarioDao
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        // Configuramos un "hilo principal" falso para las pruebas
        Dispatchers.setMain(testDispatcher)

        // Creamos una base de datos FALSA (Mock)
        usuarioDao = mockk()

        // Inicializamos el ViewModel con esa BD falsa
        viewModel = UsuarioViewModel(usuarioDao)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `login falla si los campos estan vacios`() = runTest {
        // GIVEN: Un usuario con campos vacíos
        viewModel.email = ""
        viewModel.password = ""


        // WHEN: Intentamos hacer login
        viewModel.login()
        advanceUntilIdle()

        // THEN: Debería marcar error
        assertTrue("Debería haber error", viewModel.isError)
        assertEquals("Por favor completa todos los campos", viewModel.mensajeError)
    }

    @Test
    fun `login falla si la contraseña es corta`() = runTest {
        // GIVEN: Contraseña de 3 caracteres (recordemos que pedimos min 6)
        viewModel.email = "juan@duoc.cl"
        viewModel.password = "123"

        // WHEN: Intentamos hacer login
        viewModel.login()
        advanceUntilIdle()

        // THEN: Debería marcar error de longitud
        assertTrue("Debería haber error", viewModel.isError)
        // Asegúrate que este texto sea IDÉNTICO al que pusiste en tu ViewModel
        assertEquals("La contraseña debe tener al menos 6 caracteres", viewModel.mensajeError)
    }
}