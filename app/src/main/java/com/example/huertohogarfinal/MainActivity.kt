package com.example.huertohogarfinal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.huertohogarfinal.ui.theme.HuertoHogarFinalTheme
import com.example.huertohogarfinal.ui.viewmodel.ProductoViewModel
import com.example.huertohogarfinal.ui.viewmodel.ProductoViewModelFactory
import com.example.huertohogarfinal.ui.viewmodel.UsuarioViewModel
import com.example.huertohogarfinal.ui.viewmodel.UsuarioViewModelFactory
import com.example.huertohogarfinal.ui.views.HomeScreen
import com.example.huertohogarfinal.ui.views.LoginScreen
import com.example.huertohogarfinal.ui.views.ProfileScreen
import com.example.huertohogarfinal.ui.views.CarritoScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val app = application as HuertoApp

        val usuarioViewModel: UsuarioViewModel by viewModels {
            UsuarioViewModelFactory(app.database.usuarioDao())
        }

        val productoViewModel: ProductoViewModel by viewModels {
            ProductoViewModelFactory(
                app.database.productoDao(),
                app.database.carritoDao()
            )
        }

        setContent {
            HuertoHogarFinalTheme {
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = "login") {

                    // Pantalla de Login
                    composable("login") {
                        LoginScreen(
                            viewModel = usuarioViewModel,
                            onLoginSuccess = {
                                navController.navigate("home") {
                                    popUpTo("login") { inclusive = true }
                                }
                            }
                        )
                    }

                    // Pantalla de Catálogo (Home)
                    composable("home") {
                        HomeScreen(viewModel = productoViewModel, navController = navController)
                    }

                    composable("profile") {
                        ProfileScreen(viewModel = usuarioViewModel, navController = navController)
                    }

                    composable("carrito") {
                        CarritoScreen(viewModel = productoViewModel, navController = navController)
                    }
                }
            }
        }
    }
}