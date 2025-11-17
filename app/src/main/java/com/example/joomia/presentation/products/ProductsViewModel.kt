package com.example.joomia.presentation.products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.joomia.data.repository.CartRepository
import com.example.joomia.data.repository.ProductRepository
import com.example.joomia.domain.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ProductsUiState(
    val products: List<Product> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val cartItemCount: Int = 0
)

class ProductsViewModel(
    private val productRepository: ProductRepository,
    private val cartRepository: CartRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductsUiState())
    val uiState: StateFlow<ProductsUiState> = _uiState.asStateFlow()

    init {
        loadProducts()
        observeCartCount()
    }

    private fun loadProducts() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            productRepository.getProducts()
                .onSuccess { products ->
                    _uiState.value = _uiState.value.copy(
                        products = products,
                        isLoading = false
                    )
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        error = exception.message ?: "Error al cargar productos",
                        isLoading = false
                    )
                }
        }
    }

    private fun observeCartCount() {
        viewModelScope.launch {
            cartRepository.cartItemCount.collect { count ->
                _uiState.value = _uiState.value.copy(cartItemCount = count)
            }
        }
    }

    fun addToCart(product: Product, quantity: Int) {
        viewModelScope.launch {
            cartRepository.addToCart(product, quantity)
        }
    }

    fun retry() {
        loadProducts()
    }
}
