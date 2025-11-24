package com.example.huertohogarfinal.ui.viewmodel

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.huertohogarfinal.data.dao.CarritoDao
import com.example.huertohogarfinal.data.dao.ProductoDao
import com.example.huertohogarfinal.data.entities.ItemCarrito
import com.example.huertohogarfinal.data.entities.Producto
import kotlinx.coroutines.launch
import com.example.huertohogarfinal.data.api.RetrofitClient
import com.example.huertohogarfinal.data.api.BackendClient
import com.example.huertohogarfinal.data.api.ProductoBackend

class ProductoViewModel(
    private val productoDao: ProductoDao,
    private val carritoDao: CarritoDao
) : ViewModel() {

    var listaProductos by mutableStateOf<List<Producto>>(emptyList())
    var listaCarrito by mutableStateOf<List<ItemCarrito>>(emptyList())
    var totalCarrito by mutableStateOf(0)
    var precioDolar by mutableStateOf("Cargando...")

    var listaBackend by mutableStateOf<List<ProductoBackend>>(emptyList())

    init {
        obtenerProductos()
        cargarCarrito()
        obtenerIndicadoresEconomicos()
        obtenerProductosDeMiBackend()
    }

    fun obtenerProductos() {
        viewModelScope.launch {
            listaProductos = productoDao.getAllProductos()
        }
    }

    fun agregarAlCarrito(producto: Producto, context: Context) {
        viewModelScope.launch {
            val item = ItemCarrito(nombreProducto = producto.nombre, precio = producto.precio)
            carritoDao.insertar(item)
            vibrarTelefono(context)
            cargarCarrito()
        }
    }

    fun cargarCarrito() {
        viewModelScope.launch {
            listaCarrito = carritoDao.obtenerCarrito()
            totalCarrito = listaCarrito.sumOf { it.precio }
        }
    }

    fun pagarCarrito(context: Context) {
        viewModelScope.launch {
            carritoDao.vaciarCarrito()
            cargarCarrito()
            android.widget.Toast.makeText(context, "¡Compra realizada con éxito!", android.widget.Toast.LENGTH_LONG).show()
        }
    }

    private fun vibrarTelefono(context: Context) {
        val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(100)
        }
    }

    fun obtenerIndicadoresEconomicos() {
        viewModelScope.launch {
            try {
                val respuesta = RetrofitClient.instance.obtenerIndicadores()
                precioDolar = "$${respuesta.dolar.valor}"
            } catch (e: Exception) {
                precioDolar = "Error"
                e.printStackTrace()
            }
        }
    }

    fun obtenerProductosDeMiBackend() {
        viewModelScope.launch {
            try {
                listaBackend = BackendClient.service.obtenerProductosBackend()
                println("✅ ÉXITO: Productos traídos del backend: ${listaBackend.size}")
            } catch (e: Exception) {
                println("❌ ERROR BACKEND: Asegúrate que Spring Boot esté corriendo. Error: ${e.message}")
                e.printStackTrace()
            }
        }
    }
}

class ProductoViewModelFactory(
    private val productoDao: ProductoDao,
    private val carritoDao: CarritoDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProductoViewModel(productoDao, carritoDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}