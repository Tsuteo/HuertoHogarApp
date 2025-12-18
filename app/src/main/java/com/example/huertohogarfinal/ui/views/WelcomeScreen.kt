package com.example.huertohogarfinal.ui.views

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.huertohogarfinal.R
import com.example.huertohogarfinal.ui.theme.*

@Composable
fun LogoHuertoHogar(modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_huerto),
            contentDescription = "Logo Huerto Hogar",
            modifier = Modifier
                .size(180.dp)
                .clip(RoundedCornerShape(24.dp))
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "HUERTO HOGAR",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.ExtraBold,
            color = MarronClaro,
            letterSpacing = 2.sp
        )
        Text(
            text = "DEL CAMPO A TU MESA",
            style = MaterialTheme.typography.labelSmall,
            color = GrisMedio,
            letterSpacing = 3.sp
        )
    }
}

@Composable
fun WelcomeScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateToRegister: () -> Unit,
    onNavigateToEmployee: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = BlancoSuave
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Spacer(modifier = Modifier.weight(1f))

            // 1. LOGO
            LogoHuertoHogar()

            Spacer(modifier = Modifier.height(60.dp))

            Button(
                onClick = onNavigateToLogin,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = VerdeEsmeralda,
                    contentColor = Color.White
                ),
                elevation = ButtonDefaults.buttonElevation(8.dp)
            ) {
                Text(
                    text = "Iniciar Sesión",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedButton(
                onClick = onNavigateToRegister,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(2.dp, VerdeEsmeralda),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.Transparent
                )
            ) {
                Text(
                    text = "Crear Cuenta Nueva",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = VerdeEsmeralda
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            TextButton(
                onClick = onNavigateToEmployee
            ) {
                Text(
                    text = "Acceso Personal Autorizado",
                    color = GrisMedio,
                    fontSize = 14.sp,
                    textDecoration = TextDecoration.Underline
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}