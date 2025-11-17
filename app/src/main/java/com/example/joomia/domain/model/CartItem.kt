package com.example.joomia.domain.model

data class CartItem(
    val id: Int,
    val title: String,
    val price: Double,
    val image: String,
    val quantity: Int
) {
    // Cálculo del subtotal por item
    val subtotal: Double
        get() = price * quantity
}