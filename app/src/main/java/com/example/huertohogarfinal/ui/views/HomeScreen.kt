package com.example.huertohogarfinal.ui.views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.huertohogarfinal.ui.viewmodel.ProductoViewModel
import androidx.compose.ui.platform.LocalContext
import android.widget.Toast
import androidx.compose.material.icons.filled.AccountCircle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: ProductoViewModel,
    navController: androidx.navigation.NavController
) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Catálogo") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF2E8B57),
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White
                ),
                actions = {
                    IconButton(onClick = { navController.navigate("profile") }) {
                        Icon(Icons.Default.AccountCircle, contentDescription = "Perfil")
                    }
                    IconButton(onClick = { navController.navigate("carrito") }) {
                        Icon(Icons.Default.ShoppingCart, contentDescription = "Carrito")
                    }
                }
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize()
        ) {

            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = null,
                        tint = Color(0xFF2E8B57)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "Indicador Económico",
                            style = MaterialTheme.typography.labelMedium
                        )
                        Text(
                            text = "Dólar Observado: ${viewModel.precioDolar}",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color(0xFF2E8B57)
                        )
                    }
                }
            }

            //Boton para ver si funciona el backend
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD)), // Azulito
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Prueba de Servidor Spring Boot",
                        style = MaterialTheme.typography.titleSmall,
                        color = Color.Blue
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = { viewModel.obtenerProductosDeMiBackend() },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
                    ) {
                        Text("Conectar con Backend")
                    }

                    Text(
                        text = "Productos recibidos: ${viewModel.listaBackend.size}",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(top = 8.dp)
                    )

                    if (viewModel.listaBackend.isNotEmpty()) {
                        Text(
                            text = "Ejemplo: ${viewModel.listaBackend[0].nombre}",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }

            Text(
                text = "Nuestros Productos Frescos",
                style = MaterialTheme.typography.titleLarge,
                color = Color(0xFF8B4513)
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(viewModel.listaProductos) { producto ->
                    ProductoCard(
                        nombre = producto.nombre,
                        precio = producto.precio,
                        categoria = producto.categoria,
                        onAgregarClick = {
                            viewModel.agregarAlCarrito(producto, context)
                            Toast.makeText(
                                context,
                                "Agregado: ${producto.nombre}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ProductoCard(
    nombre: String,
    precio: Int,
    categoria: String,
    onAgregarClick: () -> Unit
) {
    val context = LocalContext.current

    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF7F7F7)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {

        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.Home,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = Color(0xFF2E8B57)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = nombre,
                style = MaterialTheme.typography.bodyLarge,
                color = Color(0xFF333333)
            )
            Text(
                text = "$$precio /kg",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF666666)
            )
            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    onAgregarClick()
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD700))
            ) {
                Text("Agregar", color = Color.Black)
            }
        }
    }
}
