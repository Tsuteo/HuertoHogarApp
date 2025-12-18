package com.example.huertohogarfinal.ui.views

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.huertohogarfinal.ui.viewmodel.UsuarioViewModel
import androidx.compose.foundation.background
import com.example.huertohogarfinal.ui.theme.BlancoSuave
import com.example.huertohogarfinal.ui.theme.VerdeEsmeralda
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.text.font.FontWeight
import com.example.huertohogarfinal.ui.theme.MarronClaro
import com.example.huertohogarfinal.ui.theme.GrisMedio
import com.example.huertohogarfinal.ui.theme.GrisOscuro

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(viewModel: UsuarioViewModel, navController: NavController) {
    val usuario = viewModel.usuarioLogueado

    Column(
        modifier = Modifier.fillMaxSize().background(BlancoSuave).padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = null,
            modifier = Modifier.size(100.dp).padding(bottom = 16.dp),
            tint = VerdeEsmeralda
        )

        if (usuario != null) {
            Text(
                text = "${usuario.nombre} ${usuario.apellido}",
                style = MaterialTheme.typography.headlineMedium,
                color = MarronClaro,
                fontWeight = FontWeight.Bold
            )
            Text(usuario.correo, color = GrisMedio)
            Spacer(modifier = Modifier.height(32.dp))

            // Datos
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