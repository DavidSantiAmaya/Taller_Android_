package com.example.joomia.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.joomia.data.repository.AuthRepository
import com.example.joomia.presentation.cart.CartScreen
import com.example.joomia.presentation.cart.CartViewModel
import com.example.joomia.presentation.login.LoginScreen
import com.example.joomia.presentation.login.LoginViewModel
import com.example.joomia.presentation.products.ProductsScreen
import com.example.joomia.presentation.products.ProductsViewModel

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Products : Screen("products")
    object Cart : Screen("cart")
}

@Composable
fun NavGraph(
    authRepository: AuthRepository,
    loginViewModel: LoginViewModel,
    productsViewModel: ProductsViewModel,
    cartViewModel: CartViewModel
) {
    val navController = rememberNavController()

    // Determinar la pantalla inicial según el estado de login
    val isLoggedIn by authRepository.isLoggedIn.collectAsState(initial = false)
    val startDestination = if (isLoggedIn) Screen.Products.route else Screen.Login.route

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // Pantalla de Login
        composable(Screen.Login.route) {
            LoginScreen(
                viewModel = loginViewModel,
                onLoginSuccess = {
                    navController.navigate(Screen.Products.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        // Pantalla de Productos
        composable(Screen.Products.route) {
            ProductsScreen(
                viewModel = productsViewModel,
                onNavigateToCart = {
                    navController.navigate(Screen.Cart.route)
                }
            )
        }

        // Pantalla del Carrito
        composable(Screen.Cart.route) {
            CartScreen(
                viewModel = cartViewModel,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onCheckoutSuccess = {
                    // Después de pagar, volver a productos
                    navController.popBackStack()
                }
            )
        }
    }
}
