package com.example.huertohogarfinal.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.huertohogarfinal.data.entities.Producto
import com.example.huertohogarfinal.data.entities.Usuario
import com.example.huertohogarfinal.ui.theme.*
import com.example.huertohogarfinal.ui.viewmodel.ProductoViewModel
import com.example.huertohogarfinal.ui.viewmodel.UsuarioViewModel
import androidx.compose.material.icons.filled.Refresh

@Composable
fun AdminPanelScreen(
    usuarioViewModel: UsuarioViewModel,
    productoViewModel: ProductoViewModel,
    navController: NavController
) {
    val empleados by usuarioViewModel.listaEmpleados.collectAsState()
    val productos by productoViewModel.listaProductos.collectAsState(initial = emptyList())

    var mostrarDialogoEmpleado by remember { mutableStateOf(false) }
    var mostrarDialogoProducto by remember { mutableStateOf(false) }
    var productoEditar by remember { mutableStateOf<Producto?>(null) }

    if (productoViewModel.mensajeBackend != null) {
        AlertDialog(
            onDismissRequest = { productoViewModel.limpiarMensajeBackend() },
            title = { Text("Estado del Servidor") },
            text = { Text(productoViewModel.mensajeBackend ?: "") },
            confirmButton = {
                Button(onClick = { productoViewModel.limpiarMensajeBackend() }) {
                    Text("Entendido")
                }
            }
        )
    }

    Scaffold(
        floatingActionButton = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.End
            ) {
                FloatingActionButton(onClick = { mostrarDialogoEmpleado = true }, containerColor = VerdeEsmeralda) {
                    Icon(Icons.Default.Add, contentDescription = "Crear Empleado")
                }
                FloatingActionButton(onClick = { mostrarDialogoProducto = true }, containerColor = AmarilloMostaza) {
                    Icon(Icons.Default.Add, contentDescription = "Crear Producto")
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BlancoSuave)
                .padding(padding)
                .padding(16.dp)
        ) {
            Text(
                "Panel de Administrador",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MarronClaro
            )
            Spacer(modifier = Modifier.height(16.dp))

            Spacer(modifier = Modifier.height(16.dp))
            Card(
                colors = CardDefaults.cardColors(containerColor = VerdeEsmeralda.copy(alpha = 0.1f)),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Sincronización Nube", fontWeight = FontWeight.Bold, color = VerdeEsmeralda)
                        Text("Verificar conexión con Spring Boot", style = MaterialTheme.typography.bodySmall)
                    }

                    if (productoViewModel.cargandoBackend) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp), color = VerdeEsmeralda)
                    } else {
                        Button(
                            onClick = { productoViewModel.probarConexionBackend() },
                            colors = ButtonDefaults.buttonColors(containerColor = VerdeEsmeralda),
                            contentPadding = PaddingValues(horizontal = 12.dp)
                        ) {
                            Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp)) // Usa Refresh o Cloud
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Conectar")
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            Text("Gestión de Empleados", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.weight(1f)) {
                items(empleados) { empleado ->
                    CardEmpleado(empleado, onDelete = { usuarioViewModel.eliminarUsuario(empleado) })
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Gestión de Productos", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.weight(1f)) {
                items(productos) { producto ->
                    CardProducto(
                        producto = producto,
                        onDelete = { productoViewModel.eliminarProducto(producto) },
                        onEdit = { productoEditar = it }
                    )
                }
            }
        }
    }



    if (mostrarDialogoEmpleado) {
        DialogoCrearEmpleado(
            onDismiss = { mostrarDialogoEmpleado = false },
            onConfirm = { nom, ape, mail, pass, dir ->
                usuarioViewModel.crearEmpleado(nom, ape, mail, pass, dir)
                mostrarDialogoEmpleado = false
            }
        )
    }

    if (mostrarDialogoProducto) {
        DialogoCrearProducto(
            onDismiss = { mostrarDialogoProducto = false },
            onConfirm = { nombre, precio, stock, desc, cat, img ->
                productoViewModel.crearProducto(nombre, precio, stock, desc, cat, img)
                mostrarDialogoProducto = false
            }
        )
    }

    productoEditar?.let { producto ->
        DialogoEditarProducto(
            producto = producto,
            onDismiss = { productoEditar = null },
            onConfirm = { nombre, precio, stock, desc, cat, img ->
                productoViewModel.actualizarProducto(
                    producto.copy(
                        nombre = nombre,
                        precio = precio,
                        stock = stock,
                        descripcion = desc,
                        categoria = cat,
                        imagen = img
                    )
                )
                productoEditar = null
            }
        )
    }
}

@Composable
fun CardEmpleado(usuario: Usuario, onDelete: () -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("${usuario.nombre} ${usuario.apellido}", fontWeight = FontWeight.Bold, color = GrisOscuro)
                Text(usuario.correo, fontSize = 12.sp, color = GrisMedio)
                Text("Rol: ${usuario.rol}", fontSize = 12.sp, color = VerdeEsmeralda)
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = MaterialTheme.colorScheme.error)
            }
        }
    }
}

@Composable
fun CardProducto(producto: Producto, onDelete: () -> Unit, onEdit: (Producto) -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(producto.nombre, fontWeight = FontWeight.Bold, color = GrisOscuro)
                Text("Precio: $${producto.precio}", fontSize = 12.sp, color = GrisMedio)
                Text("Stock: ${producto.stock}", fontSize = 12.sp, color = VerdeEsmeralda)
            }
            Row {
                IconButton(onClick = { onEdit(producto) }) {
                    Icon(Icons.Default.Edit, contentDescription = "Editar", tint = AmarilloMostaza)
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}

@Composable
fun DialogoCrearEmpleado(
    onDismiss: () -> Unit,
    onConfirm: (String, String, String, String, String) -> Unit
) {
    var nom by remember { mutableStateOf("") }
    var ape by remember { mutableStateOf("") }
    var mail by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }
    var dir by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nuevo Empleado") },
        text = {
            Column {
                OutlinedTextField(nom, { nom = it }, label = { Text("Nombre") })
                OutlinedTextField(ape, { ape = it }, label = { Text("Apellido") })
                OutlinedTextField(mail, { mail = it }, label = { Text("Correo") })
                OutlinedTextField(pass, { pass = it }, label = { Text("Contraseña") })
                OutlinedTextField(dir, { dir = it }, label = { Text("Sucursal/Dirección") })
            }
        },
        confirmButton = { Button(onClick = { onConfirm(nom, ape, mail, pass, dir) }) { Text("Crear") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } }
    )
}

@Composable
fun DialogoCrearProducto(
    onDismiss: () -> Unit,
    onConfirm: (String, Int, Int, String, String, String) -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var stock by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("") }
    var imagen by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nuevo Producto") },
        text = {
            Column {
                OutlinedTextField(nombre, { nombre = it }, label = { Text("Nombre") })
                OutlinedTextField(precio, { precio = it }, label = { Text("Precio") })
                OutlinedTextField(stock, { stock = it }, label = { Text("Stock") })
                OutlinedTextField(descripcion, { descripcion = it }, label = { Text("Descripción") })
                OutlinedTextField(categoria, { categoria = it }, label = { Text("Categoría") })
                OutlinedTextField(imagen, { imagen = it }, label = { Text("Nombre imagen drawable") })
            }
        },
        confirmButton = {
            Button(onClick = {
                onConfirm(
                    nombre,
                    precio.toIntOrNull() ?: 0,
                    stock.toIntOrNull() ?: 0,
                    descripcion,
                    categoria,
                    imagen
                )
            }) { Text("Crear") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } }
    )
}

@Composable
fun DialogoEditarProducto(
    producto: Producto,
    onDismiss: () -> Unit,
    onConfirm: (String, Int, Int, String, String, String) -> Unit
) {
    var nombre by remember { mutableStateOf(producto.nombre) }
    var precio by remember { mutableStateOf(producto.precio.toString()) }
    var stock by remember { mutableStateOf(producto.stock.toString()) }
    var descripcion by remember { mutableStateOf(producto.descripcion) }
    var categoria by remember { mutableStateOf(producto.categoria) }
    var imagen by remember { mutableStateOf(producto.imagen) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Editar Producto") },
        text = {
            Column {
                OutlinedTextField(nombre, { nombre = it }, label = { Text("Nombre") })
                OutlinedTextField(precio, { precio = it }, label = { Text("Precio") })
                OutlinedTextField(stock, { stock = it }, label = { Text("Stock") })
                OutlinedTextField(descripcion, { descripcion = it }, label = { Text("Descripción") })
                OutlinedTextField(categoria, { categoria = it }, label = { Text("Categoría") })
                OutlinedTextField(imagen, { imagen = it }, label = { Text("Nombre imagen drawable") })
            }
        },
        confirmButton = {
            Button(onClick = {
                onConfirm(
                    nombre,
                    precio.toIntOrNull() ?: producto.precio,
                    stock.toIntOrNull() ?: producto.stock,
                    descripcion,
                    categoria,
                    imagen
                )
            }) { Text("Actualizar") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } }
    )
}
