package com.example.huertohogarfinal.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.huertohogarfinal.ui.theme.*
import com.example.huertohogarfinal.ui.viewmodel.UsuarioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(viewModel: UsuarioViewModel, navController: NavController) {
    val usuario = viewModel.usuarioLogueado

    var isEditing by remember { mutableStateOf(false) }

    var editNombre by remember(usuario) { mutableStateOf(usuario?.nombre ?: "") }
    var editApellido by remember(usuario) { mutableStateOf(usuario?.apellido ?: "") }
    var editDireccion by remember(usuario) { mutableStateOf(usuario?.direccion ?: "") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BlancoSuave)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                modifier = Modifier
                    .size(100.dp)
                    .align(Alignment.Center),
                tint = VerdeEsmeralda
            )

            if (usuario != null && usuario.rol != "ADMIN") {
                IconButton(
                    onClick = {
                        if (isEditing) {
                            editNombre = usuario.nombre
                            editApellido = usuario.apellido
                            editDireccion = usuario.direccion
                        }
                        isEditing = !isEditing
                    },
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Icon(
                        imageVector = if (isEditing) Icons.Default.Close else Icons.Default.Edit,
                        contentDescription = null,
                        tint = MarronClaro
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (usuario != null) {
            if (isEditing) {
                Text("Editando Perfil", style = MaterialTheme.typography.titleMedium, color = GrisMedio)
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = editNombre,
                    onValueChange = { editNombre = it },
                    label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = VerdeEsmeralda,
                        unfocusedBorderColor = MarronClaro
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = editApellido,
                    onValueChange = { editApellido = it },
                    label = { Text("Apellido") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = VerdeEsmeralda,
                        unfocusedBorderColor = MarronClaro
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = editDireccion,
                    onValueChange = { editDireccion = it },
                    label = { Text("Dirección") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = VerdeEsmeralda,
                        unfocusedBorderColor = MarronClaro
                    )
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        val usuarioActualizado = usuario.copy(
                            nombre = editNombre,
                            apellido = editApellido,
                            direccion = editDireccion
                        )
                        viewModel.actualizarUsuario(usuarioActualizado)
                        isEditing = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = VerdeEsmeralda),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Check, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Guardar Cambios")
                }

            } else {
                Text(
                    text = "${usuario.nombre} ${usuario.apellido}",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MarronClaro,
                    fontWeight = FontWeight.Bold
                )
                Text(usuario.correo, color = GrisMedio)
                Spacer(modifier = Modifier.height(32.dp))

                InfoCard("Dirección", usuario.direccion)
                InfoCard("Rol", usuario.rol)

                Spacer(modifier = Modifier.height(32.dp))

                if (usuario.rol == "ADMIN") {
                    Button(
                        onClick = { navController.navigate("admin_panel") },
                        colors = ButtonDefaults.buttonColors(containerColor = MarronClaro),
                        modifier = Modifier.fillMaxWidth().height(50.dp)
                    ) {
                        Text("Panel de Administración")
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                OutlinedButton(
                    onClick = { navController.navigate("welcome") { popUpTo(0) } },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Cerrar Sesión", color = MaterialTheme.colorScheme.error)
                }
            }
        } else {
            Text("No hay usuario logueado")
        }
    }
}

@Composable
fun InfoCard(titulo: String, valor: String) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(titulo, style = MaterialTheme.typography.labelSmall, color = VerdeEsmeralda)
            Text(valor, style = MaterialTheme.typography.bodyLarge, color = GrisOscuro)
        }
    }
}