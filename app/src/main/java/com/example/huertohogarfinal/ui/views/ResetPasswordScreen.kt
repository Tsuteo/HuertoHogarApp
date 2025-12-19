package com.example.huertohogarfinal.ui.views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.huertohogarfinal.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResetPasswordScreen(
    onVolverClick: () -> Unit,
    onResetSuccess: () -> Unit
) {
    var correo by remember { mutableStateOf("") }
    var token by remember { mutableStateOf("") }
    var nuevaContrasena by remember { mutableStateOf("") }
    var confirmarContrasena by remember { mutableStateOf("") }
    var paso by remember { mutableStateOf(1) } // 1: solicitar token, 2: resetear contraseña
    var mensaje by remember { mutableStateOf<String?>(null) }
    var mensajeError by remember { mutableStateOf<String?>(null) }
    var cargando by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Recuperar Contraseña") },
                navigationIcon = {
                    IconButton(onClick = onVolverClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = VerdeEsmeralda,
                    titleContentColor = BlancoSuave,
                    navigationIconContentColor = BlancoSuave
                )
            )
        }
    ) { padding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            color = BlancoSuave
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                if (paso == 1) {
                    // Paso 1: Solicitar código de reseteo
                    Text(
                        text = "Ingresa tu correo",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MarronClaro,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Te enviaremos un código para resetear tu contraseña",
                        color = GrisMedio,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    OutlinedTextField(
                        value = correo,
                        onValueChange = { 
                            correo = it
                            mensajeError = null
                        },
                        label = { Text("Correo Electrónico") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = VerdeEsmeralda,
                            focusedLabelColor = VerdeEsmeralda,
                            cursorColor = VerdeEsmeralda
                        ),
                        singleLine = true,
                        enabled = !cargando
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            if (correo.isBlank()) {
                                mensajeError = "Por favor ingresa tu correo"
                                return@Button
                            }
                            cargando = true
                            mensajeError = null
                            // Aquí iría la llamada al backend
                            // Por ahora simulamos
                            mensaje = "Código enviado. En desarrollo, el código es: 123456"
                            paso = 2
                            cargando = false
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = VerdeEsmeralda),
                        elevation = ButtonDefaults.buttonElevation(8.dp),
                        enabled = !cargando
                    ) {
                        if (cargando) {
                            CircularProgressIndicator(
                                color = BlancoSuave,
                                modifier = Modifier.size(24.dp)
                            )
                        } else {
                            Text("Enviar Código", fontSize = 16.sp)
                        }
                    }

                } else {
                    // Paso 2: Resetear contraseña
                    Text(
                        text = "Resetear Contraseña",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MarronClaro,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Ingresa el código y tu nueva contraseña",
                        color = GrisMedio,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    OutlinedTextField(
                        value = token,
                        onValueChange = { 
                            token = it
                            mensajeError = null
                        },
                        label = { Text("Código de Verificación") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = VerdeEsmeralda,
                            focusedLabelColor = VerdeEsmeralda,
                            cursorColor = VerdeEsmeralda
                        ),
                        singleLine = true,
                        enabled = !cargando
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = nuevaContrasena,
                        onValueChange = { 
                            nuevaContrasena = it
                            mensajeError = null
                        },
                        label = { Text("Nueva Contraseña") },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = VerdeEsmeralda,
                            focusedLabelColor = VerdeEsmeralda,
                            cursorColor = VerdeEsmeralda
                        ),
                        singleLine = true,
                        enabled = !cargando
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = confirmarContrasena,
                        onValueChange = { 
                            confirmarContrasena = it
                            mensajeError = null
                        },
                        label = { Text("Confirmar Contraseña") },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = VerdeEsmeralda,
                            focusedLabelColor = VerdeEsmeralda,
                            cursorColor = VerdeEsmeralda
                        ),
                        singleLine = true,
                        enabled = !cargando
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            if (token.isBlank() || nuevaContrasena.isBlank() || confirmarContrasena.isBlank()) {
                                mensajeError = "Por favor completa todos los campos"
                                return@Button
                            }
                            if (nuevaContrasena != confirmarContrasena) {
                                mensajeError = "Las contraseñas no coinciden"
                                return@Button
                            }
                            if (nuevaContrasena.length < 6) {
                                mensajeError = "La contraseña debe tener al menos 6 caracteres"
                                return@Button
                            }
                            cargando = true
                            mensajeError = null
                            // Aquí iría la llamada al backend
                            // Por ahora simulamos éxito
                            mensaje = "¡Contraseña actualizada exitosamente!"
                            cargando = false
                            // Esperar un momento y volver al login
                            kotlinx.coroutines.GlobalScope.launch {
                                kotlinx.coroutines.delay(2000)
                                onResetSuccess()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = VerdeEsmeralda),
                        elevation = ButtonDefaults.buttonElevation(8.dp),
                        enabled = !cargando
                    ) {
                        if (cargando) {
                            CircularProgressIndicator(
                                color = BlancoSuave,
                                modifier = Modifier.size(24.dp)
                            )
                        } else {
                            Text("Resetear Contraseña", fontSize = 16.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    TextButton(
                        onClick = { paso = 1 },
                        enabled = !cargando
                    ) {
                        Text("Solicitar nuevo código", color = MarronClaro)
                    }
                }

                // Mensajes
                if (mensaje != null) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = VerdeEsmeralda.copy(alpha = 0.1f)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = mensaje!!,
                            color = VerdeEsmeralda,
                            modifier = Modifier.padding(16.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }

                if (mensajeError != null) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = mensajeError!!,
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            modifier = Modifier.padding(16.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}
