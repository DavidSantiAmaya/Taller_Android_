package com.example.joomia.data.remote

import com.example.joomia.data.remote.dto.LoginRequest
import com.example.joomia.data.remote.dto.LoginResponse
import com.example.joomia.data.remote.dto.ProductDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * API de Fake Store
 * Base URL: https://fakestoreapi.com
 */
interface FakeStoreApi {

    /**
     * Login endpoint
     * POST https://fakestoreapi.com/auth/login
     * Body ejemplo: {"username":"johnd","password":"m38rmF$"}
     */
    @POST("auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): LoginResponse

    /**
     * Obtener todos los productos
     * GET https://fakestoreapi.com/products
     */
    @GET("products")
    suspend fun getProducts(): List<ProductDto>
}
