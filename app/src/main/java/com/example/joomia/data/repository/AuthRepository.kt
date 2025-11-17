package com.example.joomia.data.repository

import com.example.joomia.data.remote.FakeStoreApi
import com.example.joomia.data.remote.dto.LoginRequest
import com.example.joomia.util.SessionManager
import kotlinx.coroutines.flow.Flow

/**
 * Repositorio para autenticación
 */
class AuthRepository(
    private val api: FakeStoreApi,
    private val sessionManager: SessionManager
) {

    val isLoggedIn: Flow<Boolean> = sessionManager.isLoggedIn

    suspend fun login(username: String, password: String): Result<String> {
        return try {
            val response = api.login(LoginRequest(username, password))
            // Guardar sesión persistente
            sessionManager.saveSession(response.token)
            Result.success(response.token)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun logout() {
        sessionManager.clearSession()
    }
}
