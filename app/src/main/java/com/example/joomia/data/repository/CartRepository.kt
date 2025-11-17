package com.example.joomia.data.repository

import com.example.joomia.data.local.CartDao
import com.example.joomia.data.local.entity.toCartItem
import com.example.joomia.data.local.entity.toEntity
import com.example.joomia.domain.model.CartItem
import com.example.joomia.domain.model.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Repositorio para el carrito
 * Gestiona la persistencia offline con Room
 */
class CartRepository(
    private val cartDao: CartDao
) {

    // Flow que emite cambios en tiempo real del carrito
    val cartItems: Flow<List<CartItem>> = cartDao.getAllCartItems()
        .map { entities -> entities.map { it.toCartItem() } }

    val cartItemCount: Flow<Int> = cartDao.getCartItemCount()

    /**
     * Agrega un producto al carrito o actualiza la cantidad si ya existe
     */
    suspend fun addToCart(product: Product, quantity: Int) {
        val existingItem = cartDao.getCartItemById(product.id)

        if (existingItem != null) {
            // Si ya existe, actualizar cantidad
            val updatedItem = existingItem.copy(
                quantity = existingItem.quantity + quantity
            )
            cartDao.updateCartItem(updatedItem)
        } else {
            // Si no existe, insertar nuevo
            val newItem = CartItem(
                id = product.id,
                title = product.title,
                price = product.price,
                image = product.image,
                quantity = quantity
            )
            cartDao.insertCartItem(newItem.toEntity())
        }
    }

    suspend fun updateQuantity(cartItem: CartItem, newQuantity: Int) {
        if (newQuantity > 0) {
            cartDao.updateCartItem(cartItem.copy(quantity = newQuantity).toEntity())
        } else {
            removeFromCart(cartItem)
        }
    }

    suspend fun removeFromCart(cartItem: CartItem) {
        cartDao.deleteCartItem(cartItem.toEntity())
    }

    suspend fun clearCart() {
        cartDao.clearCart()
    }

    /**
     * Calcula el total del carrito
     */
    fun calculateTotal(items: List<CartItem>): Double {
        return items.sumOf { it.subtotal }
    }
}
