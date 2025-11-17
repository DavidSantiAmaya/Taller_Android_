package com.example.joomia.presentation.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.joomia.data.repository.CartRepository
import com.example.joomia.domain.model.CartItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class CartUiState(
    val cartItems: List<CartItem> = emptyList(),
    val total: Double = 0.0,
    val isEmpty: Boolean = true
)

class CartViewModel(
    private val cartRepository: CartRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CartUiState())
    val uiState: StateFlow<CartUiState> = _uiState.asStateFlow()

    init {
        observeCart()
    }

    private fun observeCart() {
        viewModelScope.launch {
            cartRepository.cartItems.collect { items ->
                _uiState.value = CartUiState(
                    cartItems = items,
                    total = cartRepository.calculateTotal(items),
                    isEmpty = items.isEmpty()
                )
            }
        }
    }

    fun updateQuantity(cartItem: CartItem, newQuantity: Int) {
        viewModelScope.launch {
            cartRepository.updateQuantity(cartItem, newQuantity)
        }
    }

    fun removeItem(cartItem: CartItem) {
        viewModelScope.launch {
            cartRepository.removeFromCart(cartItem)
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            cartRepository.clearCart()
        }
    }

    /**
     * Procesa el pago: vacía el carrito y retorna success
     */
    fun checkout(): Boolean {
        viewModelScope.launch {
            cartRepository.clearCart()
        }
        return true
    }
}
