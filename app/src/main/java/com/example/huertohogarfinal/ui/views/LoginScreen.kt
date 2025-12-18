package com.example.huertohogarfinal.ui.views

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.huertohogarfinal.ui.theme.VerdeEsmeralda
import com.example.huertohogarfinal.ui.viewmodel.UsuarioViewModel
import com.example.huertohogarfinal.ui.theme.MarronClaro
import com.example.huertohogarfinal.ui.theme.BlancoSuave
import com.example.huertohogarfinal.ui.theme.GrisMedio
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LoginScreen(
    viewModel: UsuarioViewModel,
    onLoginSuccess: () -> Unit
) {
    Surface(modifier = Modifier.fillMaxSize(), color = BlancoSuave) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = "¡Hola de nuevo!",
                style = MaterialTheme.typography.headlineMedium,
                color = MarronClaro,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text("Ingresa tus datos para continuar", color = GrisMedio)

            Spacer(modifier = Modifier.height(32.dp))


            OutlinedTextField(
                value = viewModel.correo,
                onValueChange = { viewModel.correo = it },
                label = { Text("Correo Electrónico") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = VerdeEsmeralda,
                    focusedLabelColor = VerdeEsmeralda,
                    cursorColor = VerdeEsmeralda
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = viewModel.password,
                onValueChange = { viewModel.password = it },
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = VerdeEsmeralda,
                    focusedLabelColor = VerdeEsmeralda,
                    cursorColor = VerdeEsmeralda
                ),
                singleLine = true
            )

            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                TextButton(onClick = { /* Toast logic */ }) {
                    Text("¿Olvidaste tu contraseña?", color = MarronClaro, fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { viewModel.iniciarSesionCliente(onLoginSuccess) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = VerdeEsmeralda),
                elevation = ButtonDefaults.buttonElevation(8.dp)
            ) {
                Text("Ingresar", fontSize = 16.sp)
            }

            if (viewModel.mensajeError != null) {
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = viewModel.mensajeError!!,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        modifier = Modifier.padding(16.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}