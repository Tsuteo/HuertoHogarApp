package com.example.huertohogarfinal.ui.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.huertohogarfinal.data.api.BackendClient
import com.example.huertohogarfinal.data.api.RetrofitClient
import com.example.huertohogarfinal.data.dao.CarritoDao
import com.example.huertohogarfinal.data.dao.ProductoDao
import com.example.huertohogarfinal.data.entities.ItemCarrito
import com.example.huertohogarfinal.data.entities.Producto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProductoViewModel(
    private val productoDao: ProductoDao,
    private val carritoDao: CarritoDao
) : ViewModel() {
    val listaProductos: Flow<List<Producto>> = productoDao.obtenerTodos()

    var precioDolar by mutableStateOf("Cargando...")
        private set

    init {
        obtenerValorDolar()
    }

    private fun obtenerValorDolar() {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    RetrofitClient.instance.obtenerIndicadores()
                }
                precioDolar = response.dolar.valor.toString()
            } catch (e: Exception) {
                precioDolar = "Offline"
            }
        }
    }
    val listaCarrito: Flow<List<ItemCarrito>> = carritoDao.obtenerCarrito()

    val totalCarrito: Flow<Int> = listaCarrito.map { lista ->
        lista.sumOf { it.total }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), 0)

    fun agregarAlCarrito(producto: Producto, context: Context) {
        // Validación 1: ¿Queda stock general?
        if (producto.stock <= 0) {
            Toast.makeText(context, "¡Producto agotado!", Toast.LENGTH_SHORT).show()
            return
        }

        viewModelScope.launch {
            val itemExistente = carritoDao.obtenerPorProductoId(producto.id)

            // Validación 2: ¿Ya tengo todo el stock disponible en mi carrito?
            val cantidadEnCarrito = itemExistente?.cantidad ?: 0
            if (cantidadEnCarrito >= producto.stock) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "No puedes llevar más de lo que hay en stock", Toast.LENGTH_SHORT).show()
                }
                return@launch
            }

            if (itemExistente != null) {
                val nuevoTotal = (itemExistente.cantidad + 1) * itemExistente.precioUnitario
                val itemActualizado = itemExistente.copy(
                    cantidad = itemExistente.cantidad + 1,
                    total = nuevoTotal
                )
                carritoDao.actualizar(itemActualizado)
            } else {
                val nuevoItem = ItemCarrito(
                    productoId = producto.id,
                    nombreProducto = producto.nombre,
                    precioUnitario = producto.precio,
                    cantidad = 1,
                    total = producto.precio
                )
                carritoDao.insertar(nuevoItem)
            }
        }
    }

    fun eliminarItemCarrito(item: ItemCarrito) {
        viewModelScope.launch {
            carritoDao.eliminar(item)
        }
    }

    fun pagarCarrito(context: Context) {
        viewModelScope.launch {
            val itemsCompra = listaCarrito.first()

            if (itemsCompra.isEmpty()) return@launch

            itemsCompra.forEach { item ->
                val productoOriginal = productoDao.obtenerPorId(item.productoId)
                if (productoOriginal != null) {
                    val nuevoStock = (productoOriginal.stock - item.cantidad).coerceAtLeast(0)
                    val productoActualizado = productoOriginal.copy(stock = nuevoStock)
                    productoDao.actualizar(productoActualizado)
                }
            }
            carritoDao.vaciarCarrito()
            Toast.makeText(context, "¡Compra realizada con éxito!", Toast.LENGTH_LONG).show()
        }
    }

    fun crearProducto(nombre: String, precio: Int, stock: Int, descripcion: String, categoria: String, imagen: String) {
        if (nombre.isBlank() || categoria.isBlank()) return
        viewModelScope.launch {
            val nuevoProducto = Producto(
                id = 0,
                codigo = categoria.take(2).uppercase() + System.currentTimeMillis().toString().takeLast(3),
                nombre = nombre,
                precio = precio.coerceAtLeast(0),
                stock = stock.coerceAtLeast(0),
                descripcion = descripcion,
                categoria = categoria,
                origen = "N/A",
                imagen = imagen
            )
            productoDao.insertAll(nuevoProducto)
        }
    }

    fun actualizarProducto(producto: Producto) {
        viewModelScope.launch {
            productoDao.actualizar(producto)
        }
    }

    fun eliminarProducto(producto: Producto) {
        viewModelScope.launch {
            productoDao.eliminar(producto)
        }
    }
    var mensajeBackend by mutableStateOf<String?>(null)
    var cargandoBackend by mutableStateOf(false)

    fun probarConexionBackend() {
        viewModelScope.launch {
            cargandoBackend = true
            try {
                val productosRemotos = withContext(Dispatchers.IO) {
                    BackendClient.service.obtenerProductosBackend()
                }

                val cantidadRemota = productosRemotos.size
                mensajeBackend = "¡Conexión Exitosa!\n\n" +
                        "Servidor: Operativo (Spring Boot)\n" +
                        "Productos en Nube: $cantidadRemota\n" +
                        "Sincronización: Al día."

            } catch (e: Exception) {
                mensajeBackend = "Error de Conexión:\n" +
                        "No se pudo contactar al servidor.\n\n" +
                        "Verifica que Spring Boot esté corriendo."
            } finally {
                cargandoBackend = false
            }
        }
    }

    fun limpiarMensajeBackend() {
        mensajeBackend = null
    }
}


