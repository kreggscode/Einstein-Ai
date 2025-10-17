package com.kreggscode.enstienquotes.data

import com.google.gson.annotations.SerializedName
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import java.util.concurrent.TimeUnit

// Request/Response models for Pollinations AI
data class PollinationsMessage(
    val role: String,
    val content: String
)

data class PollinationsRequest(
    val model: String = "openai",
    val messages: List<PollinationsMessage>,
    val temperature: Double = 1.0,
    val stream: Boolean = false,
    @SerializedName("private")
    val isPrivate: Boolean = false
)

data class PollinationsChoice(
    val message: PollinationsMessage,
    val index: Int,
    @SerializedName("finish_reason")
    val finishReason: String?
)

data class PollinationsResponse(
    val id: String?,
    val choices: List<PollinationsChoice>,
    val created: Long?,
    val model: String?
)

// Retrofit API interface
interface PollinationsApiService {
    @Headers("Content-Type: application/json")
    @POST("openai")
    suspend fun chat(@Body request: PollinationsRequest): PollinationsResponse
    
    companion object {
        private const val BASE_URL = "https://text.pollinations.ai/"
        
        fun create(): PollinationsApiService {
            val loggingInterceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
            
            val client = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build()
            
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            
            return retrofit.create(PollinationsApiService::class.java)
        }
    }
}
