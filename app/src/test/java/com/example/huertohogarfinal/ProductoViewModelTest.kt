package com.example.huertohogarfinal

import com.example.huertohogarfinal.data.dao.CarritoDao
import com.example.huertohogarfinal.data.dao.ProductoDao
import com.example.huertohogarfinal.data.entities.ItemCarrito
import com.example.huertohogarfinal.data.entities.Producto
import com.example.huertohogarfinal.ui.viewmodel.ProductoViewModel
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import android.widget.Toast
import kotlinx.coroutines.test.advanceUntilIdle

@OptIn(ExperimentalCoroutinesApi::class)
class ProductoViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val productoDao = mockk<ProductoDao>(relaxed = true)
    private val carritoDao = mockk<CarritoDao>(relaxed = true)
    private lateinit var viewModel: ProductoViewModel

    @org.junit.Before
    fun setup() {
        every { productoDao.obtenerTodos() } returns flowOf(emptyList())
        every { carritoDao.obtenerCarrito() } returns flowOf(emptyList())
        mockkStatic(Toast::class)
        every { Toast.makeText(any(), any<String>(), any()) } returns mockk(relaxed = true)

        viewModel = ProductoViewModel(productoDao, carritoDao)
    }

    @Test
    fun `agregarAlCarrito inserta item nuevo si no existe`() = runTest {
        val producto = Producto(1, "C01", "Manzana", 100, 10, "Desc", "Fruta", "Chile", "img")

        coEvery { carritoDao.obtenerPorProductoId(1) } returns null

        viewModel.agregarAlCarrito(producto, mockk(relaxed = true))

        coVerify {
            carritoDao.insertar(match {
                it.productoId == 1 && it.cantidad == 1 && it.total == 100
            })
        }
    }

    @Test
    fun `agregarAlCarrito actualiza cantidad si ya existe`() = runTest {
        val producto = Producto(1, "C01", "Manzana", 100, 10, "Desc", "Fruta", "Chile", "img")
        val itemExistente = ItemCarrito(10, 1, "Manzana", 100, 1, 100)

        coEvery { carritoDao.obtenerPorProductoId(1) } returns itemExistente

        viewModel.agregarAlCarrito(producto, mockk(relaxed = true))

        coVerify {
            carritoDao.actualizar(match {
                it.cantidad == 2 && it.total == 200 // 2 * 100
            })
        }
    }

    @Test
    fun `pagarCarrito vacia la tabla`() = runTest {
        val itemFalso = ItemCarrito(
            id = 1,
            productoId = 100,
            nombreProducto = "Manzana",
            precioUnitario = 100,
            cantidad = 1,
            total = 100
        )
        coEvery { carritoDao.obtenerCarrito() } returns flowOf(listOf(itemFalso))
        coEvery { carritoDao.vaciarCarrito() } just Runs
        coEvery { productoDao.obtenerPorId(any()) } returns Producto(
            id = 100, codigo = "C1", nombre = "Manzana", precio = 100, stock = 10,
            descripcion = "D", categoria = "F", origen = "O", imagen = "img"
        )
        coEvery { productoDao.actualizar(any()) } just Runs
        val viewModelTest = ProductoViewModel(productoDao, carritoDao)
        viewModelTest.pagarCarrito(mockk(relaxed = true))
        advanceUntilIdle()
        coVerify { carritoDao.vaciarCarrito() }
    }

    @Test
    fun `eliminarProducto llama al dao correctamente`() = runTest {
        val prod = Producto(1, "A", "B", 1, 1, "D", "C", "O", "I")
        viewModel.eliminarProducto(prod)
        coVerify { productoDao.eliminar(prod) }
    }
}