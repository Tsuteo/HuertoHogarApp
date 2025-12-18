package com.example.huertohogarfinal.ui.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.huertohogarfinal.data.api.RetrofitClient
import com.example.huertohogarfinal.data.dao.CarritoDao
import com.example.huertohogarfinal.data.dao.ProductoDao
import com.example.huertohogarfinal.data.entities.ItemCarrito
import com.example.huertohogarfinal.data.entities.Producto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
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
        viewModelScope.launch {
            val itemExistente = carritoDao.obtenerPorProductoId(producto.id)

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
            carritoDao.vaciarCarrito()
            Toast.makeText(context, "¡Compra realizada con éxito!", Toast.LENGTH_LONG).show()
        }
    }
}