package com.example.huertohogarfinal.ui.views

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.huertohogarfinal.data.entities.Producto
import com.example.huertohogarfinal.ui.theme.*
import com.example.huertohogarfinal.ui.viewmodel.ProductoViewModel
import com.example.huertohogarfinal.ui.viewmodel.UsuarioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: ProductoViewModel,
    usuarioViewModel: UsuarioViewModel,
    navController: androidx.navigation.NavController
) {
    val context = LocalContext.current
    val productos by viewModel.listaProductos.collectAsState(initial = emptyList())
    val usuario = usuarioViewModel.usuarioLogueado

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Catálogo", fontWeight = FontWeight.Bold, color = BlancoSuave) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = VerdeEsmeralda,
                    actionIconContentColor = BlancoSuave
                ),
                actions = {
                    IconButton(onClick = {
                        if (usuario != null) {
                            navController.navigate("profile")
                        } else {
                            navController.navigate("welcome")
                        }
                    }) {
                        Icon(
                            imageVector = if (usuario != null) Icons.Default.AccountCircle else Icons.Default.Lock,
                            contentDescription = "Perfil",
                            modifier = Modifier.size(28.dp)
                        )
                    }
                    IconButton(onClick = {
                        if (usuario != null) {
                            navController.navigate("carrito")
                        } else {
                            Toast.makeText(context, "Inicia sesión para ver tu carrito", Toast.LENGTH_SHORT).show()
                        }
                    }) {
                        BadgedBox(badge = {}) {
                            Icon(Icons.Default.ShoppingCart, contentDescription = "Carrito", modifier = Modifier.size(28.dp))
                        }
                    }
                }
            )
        },
        containerColor = BlancoSuave
    ) { paddingValues ->

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(paddingValues).fillMaxSize()
        ) {

            if (usuario == null) {
                item(span = { GridItemSpan(2) }) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = AmarilloMostaza),
                        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                    ) {
                        Text(
                            text = "Estás en Modo Invitado. Inicia sesión para comprar.",
                            style = MaterialTheme.typography.labelMedium,
                            color = GrisOscuro,
                            modifier = Modifier.padding(12.dp),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

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
                            modifier = Modifier.size(48.dp).clip(RoundedCornerShape(8.dp)).background(VerdeEsmeralda.copy(alpha = 0.1f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Info, contentDescription = null, tint = VerdeEsmeralda)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("Indicador Económico", style = MaterialTheme.typography.labelSmall, color = GrisMedio)
                            Text("Dólar hoy: $${viewModel.precioDolar}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = GrisOscuro)
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
                    producto = producto,
                    esInvitado = (usuario == null),
                    onAgregarClick = {
                        if (usuario != null) {
                            viewModel.agregarAlCarrito(producto, context)
                            if(producto.stock > 0) {
                                Toast.makeText(context, "Intentando agregar...", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(context, "Modo Invitado: Debes registrarte para comprar", Toast.LENGTH_SHORT).show()
                        }
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
    producto: Producto,
    esInvitado: Boolean,
    onAgregarClick: () -> Unit
) {
    val context = LocalContext.current
    val resId = context.resources.getIdentifier(producto.imagen, "drawable", context.packageName)

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
                modifier = Modifier.size(80.dp).clip(RoundedCornerShape(8.dp)).background(BlancoSuave),
                contentAlignment = Alignment.Center
            ) {
                if (resId != 0) {
                    Image(
                        painter = painterResource(id = resId),
                        contentDescription = producto.nombre,
                        modifier = Modifier.size(64.dp)
                    )
                } else {
                    Box(
                        modifier = Modifier.size(64.dp).background(Color.LightGray),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No Img", fontSize = 12.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = producto.nombre,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = GrisOscuro,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = producto.categoria,
                style = MaterialTheme.typography.labelSmall,
                color = GrisMedio,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "$${producto.precio} /kg",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                color = MarronClaro
            )

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = if (producto.stock > 0) "Stock: ${producto.stock}" else "¡AGOTADO!",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = if (producto.stock > 0) VerdeEsmeralda else MaterialTheme.colorScheme.error
            )

            Spacer(modifier = Modifier.height(12.dp))

            val hayStock = producto.stock > 0

            Button(
                onClick = onAgregarClick,
                enabled = hayStock,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (esInvitado) GrisMedio else AmarilloMostaza,
                    contentColor = if (esInvitado) Color.White else GrisOscuro,
                    disabledContainerColor = Color.LightGray,
                    disabledContentColor = Color.White
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth().height(36.dp),
                contentPadding = PaddingValues(horizontal = 8.dp)
            ) {
                Icon(
                    imageVector = if (!hayStock) Icons.Default.Close else if (esInvitado) Icons.Default.Lock else Icons.Default.Add,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = if (!hayStock) "Sin Stock" else if (esInvitado) "Acceder" else "Agregar",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}