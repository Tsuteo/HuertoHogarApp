package com.example.huertohogarfinal.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.huertohogarfinal.data.entities.Producto
import com.example.huertohogarfinal.ui.theme.*
import com.example.huertohogarfinal.ui.viewmodel.ProductoViewModel
import com.example.huertohogarfinal.ui.viewmodel.UsuarioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SellerScreen(
    productoViewModel: ProductoViewModel,
    usuarioViewModel: UsuarioViewModel,
    navController: NavController
) {
    val productos by productoViewModel.listaProductos.collectAsState(initial = emptyList())
    val productosOrdenados = productos.sortedBy { it.stock }

    val usuario = usuarioViewModel.usuarioLogueado

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Modo Vendedor", fontWeight = FontWeight.Bold)
                        Text("Control de Inventario", style = MaterialTheme.typography.labelSmall)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = VerdeEsmeralda,
                    titleContentColor = BlancoSuave,
                    actionIconContentColor = BlancoSuave
                ),
                actions = {
                    IconButton(onClick = {
                        navController.navigate("welcome") { popUpTo(0) }
                    }) {
                        Icon(Icons.Default.ExitToApp, contentDescription = "Salir")
                    }
                }
            )
        },
        containerColor = BlancoSuave
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {

            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            ) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Hola, ${usuario?.nombre ?: "Vendedor"}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MarronClaro
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    val criticos = productos.count { it.stock <= 5 }
                    if (criticos > 0) {
                        Badge(containerColor = MaterialTheme.colorScheme.error) {
                            Text("$criticos Críticos", color = Color.White, modifier = Modifier.padding(4.dp))
                        }
                    }
                }
            }

            Text(
                "Reposición Rápida",
                style = MaterialTheme.typography.titleMedium,
                color = GrisOscuro,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(productosOrdenados) { producto ->
                    RestockCard(producto = producto, onRestock = { cantidad ->
                        // Lógica simple: sumar stock
                        val nuevoProd = producto.copy(stock = producto.stock + cantidad)
                        productoViewModel.actualizarProducto(nuevoProd)
                    })
                }
            }
        }
    }
}

@Composable
fun RestockCard(producto: Producto, onRestock: (Int) -> Unit) {
    val esCritico = producto.stock <= 5

    Card(
        colors = CardDefaults.cardColors(containerColor = if (esCritico) Color(0xFFFFEBEE) else Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(producto.nombre, fontWeight = FontWeight.Bold, color = GrisOscuro)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Stock actual: ", fontSize = 12.sp, color = GrisMedio)
                    Text(
                        "${producto.stock}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (esCritico) MaterialTheme.colorScheme.error else VerdeEsmeralda
                    )
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = { onRestock(10) },
                    colors = ButtonDefaults.buttonColors(containerColor = VerdeEsmeralda),
                    contentPadding = PaddingValues(horizontal = 12.dp),
                    modifier = Modifier.height(36.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(14.dp))
                    Text("10", fontSize = 12.sp)
                }

                Button(
                    onClick = { onRestock(50) },
                    colors = ButtonDefaults.buttonColors(containerColor = AmarilloMostaza),
                    contentPadding = PaddingValues(horizontal = 12.dp),
                    modifier = Modifier.height(36.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(14.dp))
                    Text("50", fontSize = 12.sp, color = GrisOscuro)
                }
            }
        }
    }
}