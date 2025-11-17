package com.example.joomia.data.remote.dto

import com.example.joomia.domain.model.Product
import com.example.joomia.domain.model.Rating

data class ProductDto(
    val id: Int,
    val title: String,
    val price: Double,
    val description: String,
    val category: String,
    val image: String,
    val rating: RatingDto?
)

data class RatingDto(
    val rate: Double,
    val count: Int
)

// Mapper: convierte DTO a modelo de dominio
fun ProductDto.toProduct(): Product {
    return Product(
        id = id,
        title = title,
        price = price,
        description = description,
        category = category,
        image = image,
        rating = rating?.let { Rating(it.rate, it.count) }
    )
}