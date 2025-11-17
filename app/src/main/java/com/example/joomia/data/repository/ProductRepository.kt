package com.example.joomia.data.repository

import com.example.joomia.data.remote.FakeStoreApi
import com.example.joomia.data.remote.dto.toProduct
import com.example.joomia.domain.model.Product

/**
 * Repositorio para productos
 */
class ProductRepository(
    private val api: FakeStoreApi
) {

    suspend fun getProducts(): Result<List<Product>> {
        return try {
            val products = api.getProducts().map { it.toProduct() }
            Result.success(products)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
