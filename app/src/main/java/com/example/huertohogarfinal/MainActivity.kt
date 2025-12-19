package com.example.huertohogarfinal

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.huertohogarfinal.data.db.HuertoHogarDatabase
import com.example.huertohogarfinal.ui.theme.HuertoHogarFinalTheme
import com.example.huertohogarfinal.ui.viewmodel.ProductoViewModel
import com.example.huertohogarfinal.ui.viewmodel.UsuarioViewModel
import com.example.huertohogarfinal.ui.views.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = HuertoHogarDatabase.getDatabase(applicationContext, lifecycleScope)
        val usuarioViewModel = UsuarioViewModel(database.usuarioDao())
        val productoViewModel = ProductoViewModel(database.productoDao(), database.carritoDao())

        setContent {
            HuertoHogarFinalTheme {
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = "welcome") {

                    composable("welcome") {
                        WelcomeScreen(
                            onNavigateToLogin = {
                                usuarioViewModel.limpiarCampos()
                                navController.navigate("login")
                            },
                            onNavigateToRegister = {
                                usuarioViewModel.limpiarCampos()
                                navController.navigate("register")
                            },
                            onNavigateToEmployee = {
                                usuarioViewModel.limpiarCampos()
                                navController.navigate("employee_login")
                            }
                        )
                    }

                    composable("login") {
                        LoginScreen(
                            viewModel = usuarioViewModel,
                            onLoginSuccess = {
                                navController.navigate("home") {
                                    popUpTo("welcome") { inclusive = true }
                                }
                            },
                            onNavigateToResetPassword = {
                                navController.navigate("reset_password")
                            }
                        )
                    }

                    composable("reset_password") {
                        ResetPasswordScreen(
                            onVolverClick = {
                                navController.popBackStack()
                            },
                            onResetSuccess = {
                                navController.navigate("login") {
                                    popUpTo("login") { inclusive = true }
                                }
                                Toast.makeText(applicationContext, "Contraseña actualizada", Toast.LENGTH_SHORT).show()
                            }
                        )
                    }

                    composable("register") {
                        RegisterScreen(
                            viewModel = usuarioViewModel,
                            onRegisterSuccess = {
                                navController.navigate("login")
                                Toast.makeText(applicationContext, "Cuenta creada con éxito", Toast.LENGTH_SHORT).show()
                            }
                        )
                    }

                    composable("employee_login") {
                        EmployeeLoginScreen(
                            viewModel = usuarioViewModel,
                            onLoginSuccess = {
                                navController.navigate("home") {
                                    popUpTo("welcome") { inclusive = true }
                                }
                            }
                        )
                    }

                    composable("home") {
                        HomeScreen(
                            viewModel = productoViewModel,
                            navController = navController
                        )
                    }

                    composable("profile") {
                        ProfileScreen(
                            viewModel = usuarioViewModel,
                            navController = navController
                        )
                    }

                    composable("carrito") {
                        CarritoScreen(
                            viewModel = productoViewModel,
                            navController = navController
                        )
                    }

                    composable("admin_panel") {
                        AdminPanelScreen(
                            usuarioViewModel = usuarioViewModel,
                            productoViewModel = productoViewModel,
                            navController = navController
                        )
                    }
                }
            }
        }
    }
}
