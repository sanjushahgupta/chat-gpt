package com.digitalaya.chat
import com.digitalaya.chat.model.ChatRequest
import com.digitalaya.chat.model.ChatResponse
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

interface ChatInterface {
    @Headers(
        "Content-Type: application/json"
    )
    @POST("completions")
    suspend fun sendMessage(
        @Header("Authorization") token: String,
        @Body message: ChatRequest
    ): ChatResponse

    object RetrofitObj {
        private const val BaseUrl = "https://api.openai.com/v1/chat/"

        private val client: OkHttpClient = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS) // Set connection timeout
            .readTimeout(30, TimeUnit.SECONDS) // Set read timeout
            .writeTimeout(30, TimeUnit.SECONDS) // Set write timeout
            .build()

        // Create a Retrofit instance with the OkHttpClient
        private val retrofit = Retrofit.Builder()
            .baseUrl(BaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        val openAIAPI: ChatInterface = retrofit.create(ChatInterface::class.java)
    }
}
