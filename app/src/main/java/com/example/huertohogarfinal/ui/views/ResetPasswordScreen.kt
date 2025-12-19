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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
    var paso by remember { mutableStateOf(1) }
    var mensaje by remember { mutableStateOf<String?>(null) }
    var mensajeError by remember { mutableStateOf<String?>(null) }
    var cargando by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Recuperar Contraseña", color = BlancoSuave, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onVolverClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver", tint = BlancoSuave)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = VerdeEsmeralda
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

                            scope.launch {
                                cargando = true
                                mensajeError = null
                                delay(1500)
                                mensaje = "Código enviado: 123456"
                                paso = 2
                                cargando = false
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
                            Text("Enviar Código", fontSize = 16.sp)
                        }
                    }

                } else {
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

                            scope.launch {
                                cargando = true
                                mensajeError = null
                                delay(2000)

                                mensaje = "¡Contraseña actualizada exitosamente!"
                                cargando = false

                                delay(1500)
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