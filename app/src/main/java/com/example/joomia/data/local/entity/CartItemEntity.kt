package com.example.joomia.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.joomia.domain.model.CartItem

/**
 * Entidad Room para almacenar items del carrito offline
 */
@Entity(tableName = "cart_items")
data class CartItemEntity(
    @PrimaryKey
    val id: Int,
    val title: String,
    val price: Double,
    val image: String,
    val quantity: Int
)

// Mappers entre Entity y Domain Model
fun CartItemEntity.toCartItem(): CartItem {
    return CartItem(
        id = id,
        title = title,
        price = price,
        image = image,
        quantity = quantity
    )
}

fun CartItem.toEntity(): CartItemEntity {
    return CartItemEntity(
        id = id,
        title = title,
        price = price,
        image = image,
        quantity = quantity
    )
}
