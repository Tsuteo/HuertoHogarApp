package com.example.huertohogarfinal.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.huertohogarfinal.data.entities.Usuario
import com.example.huertohogarfinal.ui.theme.*
import com.example.huertohogarfinal.ui.viewmodel.UsuarioViewModel

@Composable
fun AdminPanelScreen(viewModel: UsuarioViewModel, navController: NavController) {
    val empleados by viewModel.listaEmpleados.collectAsState()
    var mostrarDialogo by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { mostrarDialogo = true },
                containerColor = VerdeEsmeralda,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Crear Empleado")
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
                "Gestión de Personal",
                style = MaterialTheme.typography.headlineMedium,
                color = MarronClaro,
                fontWeight = FontWeight.Bold
            )
            Text("Panel de Administrador", style = MaterialTheme.typography.bodyMedium, color = GrisMedio)

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(empleados) { empleado ->
                    CardEmpleado(empleado, onDelete = { viewModel.eliminarUsuario(empleado) })
                }
            }
        }
    }

    if (mostrarDialogo) {
        DialogoCrearEmpleado(
            onDismiss = { mostrarDialogo = false },
            onConfirm = { nom, ape, mail, pass, dir ->
                viewModel.crearEmpleado(nom, ape, mail, pass, dir)
                mostrarDialogo = false
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
                Text(
                    text = "${usuario.nombre} ${usuario.apellido}",
                    fontWeight = FontWeight.Bold,
                    color = GrisOscuro
                )
                Text(text = usuario.correo, fontSize = 12.sp, color = GrisMedio)
                Text(text = "Rol: ${usuario.rol}", fontSize = 12.sp, color = VerdeEsmeralda)
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = MaterialTheme.colorScheme.error)
            }
        }
    }
}

@Composable
fun DialogoCrearEmpleado(onDismiss: () -> Unit, onConfirm: (String, String, String, String, String) -> Unit) {
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
                OutlinedTextField(value = nom, onValueChange = { nom = it }, label = { Text("Nombre") })
                OutlinedTextField(value = ape, onValueChange = { ape = it }, label = { Text("Apellido") })
                OutlinedTextField(value = mail, onValueChange = { mail = it }, label = { Text("Correo") })
                OutlinedTextField(value = pass, onValueChange = { pass = it }, label = { Text("Contraseña") })
                OutlinedTextField(value = dir, onValueChange = { dir = it }, label = { Text("Sucursal/Dirección") })
            }
        },
        confirmButton = {
            Button(onClick = { onConfirm(nom, ape, mail, pass, dir) }) { Text("Crear") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}