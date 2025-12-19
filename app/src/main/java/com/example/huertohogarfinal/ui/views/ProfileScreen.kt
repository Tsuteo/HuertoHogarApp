package com.example.huertohogarfinal.ui.views

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.huertohogarfinal.ui.theme.*
import com.example.huertohogarfinal.ui.viewmodel.UsuarioViewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Objects

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(viewModel: UsuarioViewModel, navController: NavController) {
    val usuario = viewModel.usuarioLogueado
    val context = LocalContext.current

    var capturedImageUri by remember { mutableStateOf<Uri?>(null) }
    var tempPhotoUri by remember { mutableStateOf<Uri?>(null) }

    var isEditing by remember { mutableStateOf(false) }
    var editNombre by remember(usuario) { mutableStateOf(usuario?.nombre ?: "") }
    var editApellido by remember(usuario) { mutableStateOf(usuario?.apellido ?: "") }
    var editDireccion by remember(usuario) { mutableStateOf(usuario?.direccion ?: "") }

    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success && tempPhotoUri != null) {
            capturedImageUri = tempPhotoUri
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            val uri = crearArchivoImagen(context)
            tempPhotoUri = uri
            cameraLauncher.launch(uri)
        } else {
            Toast.makeText(context, "Se requiere permiso de cámara", Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BlancoSuave)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            contentAlignment = Alignment.BottomEnd,
            modifier = Modifier.size(110.dp)
        ) {
            val fotoAMostrar = if (capturedImageUri != null) {
                capturedImageUri // Foto recién tomada
            } else if (usuario?.fotoPerfil != null) {
                Uri.parse(usuario.fotoPerfil)
            } else {
                null
            }

            if (fotoAMostrar != null) {
                Image(
                    painter = rememberAsyncImagePainter(fotoAMostrar),
                    contentDescription = "Foto Perfil",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .border(2.dp, VerdeEsmeralda, CircleShape),
                    contentScale = ContentScale.Crop
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(Color.LightGray)
                        .padding(10.dp),
                    tint = Color.White
                )
            }

            if (isEditing) {
                IconButton(
                    onClick = {
                        val permissionCheckResult = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                        if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                            val uri = crearArchivoImagen(context)
                            tempPhotoUri = uri
                            cameraLauncher.launch(uri)
                        } else {
                            permissionLauncher.launch(Manifest.permission.CAMERA)
                        }
                    },
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(VerdeEsmeralda)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Tomar Foto", tint = Color.White, modifier = Modifier.size(20.dp))
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (usuario != null && !isEditing) {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                TextButton(onClick = {
                    isEditing = true
                    editNombre = usuario.nombre
                    editApellido = usuario.apellido
                    editDireccion = usuario.direccion
                }) {
                    Icon(Icons.Default.Edit, contentDescription = null, tint = MarronClaro)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Editar Perfil", color = MarronClaro)
                }
            }
        }

        if (usuario != null) {
            if (isEditing) {
                Text("Editando tus datos", style = MaterialTheme.typography.titleMedium, color = GrisMedio)
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = editNombre,
                    onValueChange = { editNombre = it },
                    label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = editApellido,
                    onValueChange = { editApellido = it },
                    label = { Text("Apellido") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = editDireccion,
                    onValueChange = { editDireccion = it },
                    label = { Text("Dirección") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        val nuevaFotoString = capturedImageUri?.toString() ?: usuario.fotoPerfil

                        val usuarioActualizado = usuario.copy(
                            nombre = editNombre,
                            apellido = editApellido,
                            direccion = editDireccion,
                            fotoPerfil = nuevaFotoString
                        )
                        viewModel.actualizarUsuario(usuarioActualizado)
                        isEditing = false
                        Toast.makeText(context, "Perfil actualizado", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = VerdeEsmeralda),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Check, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Guardar Cambios")
                }

                Spacer(modifier = Modifier.height(8.dp))

                TextButton(onClick = { isEditing = false }) {
                    Text("Cancelar", color = MaterialTheme.colorScheme.error)
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

fun crearArchivoImagen(context: Context): Uri {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"
    val storageDir = context.getExternalFilesDir("my_images")
    val image = File.createTempFile(
        imageFileName,
        ".jpg",
        storageDir
    )
    return FileProvider.getUriForFile(
        Objects.requireNonNull(context),
        context.packageName + ".provider",
        image
    )
}
@Composable
fun InfoCard(titulo: String, valor: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = titulo,
                style = MaterialTheme.typography.labelSmall,
                color = VerdeEsmeralda
            )
            Text(
                text = valor,
                style = MaterialTheme.typography.bodyLarge,
                color = GrisOscuro
            )
        }
    }
}
