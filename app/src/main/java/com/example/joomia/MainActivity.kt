package com.example.joomia

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.joomia.data.local.CartDatabase
import com.example.joomia.data.remote.FakeStoreApi
import com.example.joomia.data.repository.AuthRepository
import com.example.joomia.data.repository.CartRepository
import com.example.joomia.data.repository.ProductRepository
import com.example.joomia.presentation.cart.CartViewModel
import com.example.joomia.presentation.login.LoginViewModel
import com.example.joomia.presentation.navigation.NavGraph
import com.example.joomia.presentation.products.ProductsViewModel
import com.example.joomia.ui.theme.JoomiaTheme
import com.example.joomia.util.SessionManager
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Activity principal de la aplicación
 * Configura todas las dependencias (Room, Retrofit, Repositorios, ViewModels)
 * e inicia el NavGraph
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializar Room Database para persistencia offline
        val database = CartDatabase.getDatabase(applicationContext)
        val cartDao = database.cartDao()

        // Inicializar Retrofit para llamadas a Fake Store API
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://fakestoreapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

        val fakeStoreApi = retrofit.create(FakeStoreApi::class.java)

        // Inicializar SessionManager para persistencia de sesión
        val sessionManager = SessionManager(applicationContext)

        // Inicializar Repositorios
        val authRepository = AuthRepository(fakeStoreApi, sessionManager)
        val productRepository = ProductRepository(fakeStoreApi)
        val cartRepository = CartRepository(cartDao)

        // Inicializar ViewModels
        val loginViewModel = LoginViewModel(authRepository)
        val productsViewModel = ProductsViewModel(productRepository, cartRepository)
        val cartViewModel = CartViewModel(cartRepository)

        setContent {
            JoomiaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavGraph(
                        authRepository = authRepository,
                        loginViewModel = loginViewModel,
                        productsViewModel = productsViewModel,
                        cartViewModel = cartViewModel
                    )
                }
            }
        }
    }
}
