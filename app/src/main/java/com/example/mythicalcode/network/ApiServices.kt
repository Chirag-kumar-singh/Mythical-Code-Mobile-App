package com.example.mythicalcode.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

// Define the API interface
interface ApiService {
    @POST("api/run") // Endpoint relative to the base URL
    suspend fun runCode(@Body request: CodeRequest): CodeResponse
}

// Data classes for request and response
data class CodeRequest(val lang: String, val code: String, val input: String)

// This is how the response should be structured. Update it if your backend changes.
data class CodeResponse(
    val success: Boolean,
    val message: String,
    val output: String,
    val error: ErrorWrapper? // Adjusting this to match the nested structure
)

data class ErrorWrapper(
    val error: ErrorResponse? // The nested error object
)

data class ErrorResponse(
    val code: Int,
    val killed: Boolean,
    val signal: String?,
    val cmd: String?,
    val stderr: String? // This is where your detailed error message will be
)


// Create Retrofit instance
private val retrofit = Retrofit.Builder()
    .baseUrl("http://192.168.137.52:8000/") // Use 10.0.2.2 for emulator or your IP for a real device
    .addConverterFactory(GsonConverterFactory.create())
    .build()

// Create ApiService instance
val apiService: ApiService = retrofit.create(ApiService::class.java)
