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


    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: UsuarioViewModel
    private lateinit var usuarioDao: UsuarioDao
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        usuarioDao = mockk()
        viewModel = UsuarioViewModel(usuarioDao)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `login falla si los campos estan vacios`() = runTest {
        viewModel.email = ""
        viewModel.password = ""
        viewModel.login()
        advanceUntilIdle()
        assertTrue("Debería haber error", viewModel.isError)
        assertEquals("Por favor completa todos los campos", viewModel.mensajeError)
    }

    @Test
    fun `login falla si la contraseña es corta`() = runTest {
        viewModel.email = "juan@duoc.cl"
        viewModel.password = "1234"
        viewModel.login()
        advanceUntilIdle()

        assertTrue("Debería haber error", viewModel.isError)
        assertEquals("La contraseña debe tener al menos 6 caracteres", viewModel.mensajeError)
    }
}