package com.example.huertohogarfinal.ui.views

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.huertohogarfinal.ui.theme.*
import com.example.huertohogarfinal.ui.viewmodel.ProductoViewModel
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Add

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: ProductoViewModel,
    navController: androidx.navigation.NavController
) {
    val context = LocalContext.current
    val productos by viewModel.listaProductos.collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Catálogo",
                        fontWeight = FontWeight.Bold,
                        color = BlancoSuave
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = VerdeEsmeralda,
                    actionIconContentColor = BlancoSuave
                ),
                actions = {
                    IconButton(onClick = { navController.navigate("profile") }) {
                        Icon(Icons.Default.AccountCircle, contentDescription = "Perfil", modifier = Modifier.size(28.dp))
                    }
                    IconButton(onClick = { navController.navigate("carrito") }) {
                        BadgedBox(
                            badge = {

                            }
                        ) {
                            Icon(Icons.Default.ShoppingCart, contentDescription = "Carrito", modifier = Modifier.size(28.dp))
                        }
                    }
                }
            )
        },
        containerColor = BlancoSuave // Fondo Crema/Hueso
    ) { paddingValues ->

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {

            item(span = { GridItemSpan(2) }) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(2.dp),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(VerdeEsmeralda.copy(alpha = 0.1f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null,
                                tint = VerdeEsmeralda
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Indicador Económico",
                                style = MaterialTheme.typography.labelSmall,
                                color = GrisMedio
                            )
                            Text(
                                text = "Dólar hoy: $${viewModel.precioDolar}",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = GrisOscuro
                            )
                        }
                    }
                }
            }

            item(span = { GridItemSpan(2) }) {
                Text(
                    text = "Nuestros Productos Frescos",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MarronClaro,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            items(productos) { producto ->
                ProductoCard(
                    nombre = producto.nombre,
                    precio = producto.precio,
                    categoria = producto.categoria,
                    onAgregarClick = {
                        viewModel.agregarAlCarrito(producto, context)
                        Toast.makeText(context, "¡${producto.nombre} agregado!", Toast.LENGTH_SHORT).show()
                    }
                )
            }

            item(span = { GridItemSpan(2) }) {
                Spacer(modifier = Modifier.height(24.dp))
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
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(BlancoSuave),
                contentAlignment = Alignment.Center
            ) {
                val icono = when {
                    categoria.contains("Fruta") -> Icons.Default.Home
                    categoria.contains("Miel") -> Icons.Default.Home
                    else -> Icons.Default.Home
                }

                Icon(
                    imageVector = icono,
                    contentDescription = null,
                    modifier = Modifier.size(40.dp),
                    tint = VerdeEsmeralda
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = nombre,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = GrisOscuro,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )


            Text(
                text = categoria,
                style = MaterialTheme.typography.labelSmall,
                color = GrisMedio,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "$$precio /kg", // Ajusta si es unidad o kg
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                color = MarronClaro
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onAgregarClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = AmarilloMostaza,
                    contentColor = GrisOscuro
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth().height(36.dp),
                contentPadding = PaddingValues(horizontal = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("Agregar", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
