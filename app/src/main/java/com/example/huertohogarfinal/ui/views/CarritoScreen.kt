package com.example.huertohogarfinal.ui.views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.huertohogarfinal.data.entities.ItemCarrito
import com.example.huertohogarfinal.ui.theme.*
import com.example.huertohogarfinal.ui.viewmodel.ProductoViewModel
import androidx.compose.material.icons.filled.ShoppingCart

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarritoScreen(viewModel: ProductoViewModel, navController: NavController) {
    val context = LocalContext.current

    val itemsCarrito by viewModel.listaCarrito.collectAsState(initial = emptyList())
    val total by viewModel.totalCarrito.collectAsState(initial = 0)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Mi Carrito", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = VerdeEsmeralda,
                    titleContentColor = BlancoSuave,
                    navigationIconContentColor = BlancoSuave
                )
            )
        },
        bottomBar = {
            if (itemsCarrito.isNotEmpty()) {
                Surface(
                    color = Color.White,
                    shadowElevation = 16.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Total a Pagar", style = MaterialTheme.typography.labelMedium, color = GrisMedio)
                            Text(
                                text = "$$total",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                color = VerdeEsmeralda
                            )
                        }
                        Button(
                            onClick = {
                                viewModel.pagarCarrito(context)
                                navController.navigate("home") {
                                    popUpTo("home") { inclusive = true }
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = AmarilloMostaza),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.height(50.dp)
                        ) {
                            Text("Pagar Ahora", color = GrisOscuro, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        },
        containerColor = BlancoSuave
    ) { paddingValues ->

        if (itemsCarrito.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = null,
                    modifier = Modifier.size(100.dp),
                    tint = GrisMedio.copy(alpha = 0.5f)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text("Tu carrito está vacío", style = MaterialTheme.typography.titleMedium, color = GrisMedio)
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = { navController.popBackStack() }, colors = ButtonDefaults.buttonColors(containerColor = VerdeEsmeralda)) {
                    Text("Ir a comprar")
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(itemsCarrito) { item ->
                    ItemCarritoCard(item = item, onDelete = { viewModel.eliminarItemCarrito(item) })
                }
            }
        }
    }
}

@Composable
fun ItemCarritoCard(item: ItemCarrito, onDelete: () -> Unit) {
    Card(
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.nombreProducto,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = GrisOscuro
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${item.cantidad} x $${item.precioUnitario}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = GrisMedio
                )
                Text(
                    text = "Subtotal: $${item.total}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = VerdeEsmeralda
                )
            }

            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = ErrorRed)
            }
        }
    }
}