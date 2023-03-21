package com.digitalaya.chat

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import com.digitalaya.chat.model.ChatRequest
import com.digitalaya.chat.model.ChatResponse
import com.digitalaya.chat.model.Message.Message
import com.digitalaya.chat.util.UserPreference
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import retrofit2.http.*

interface ChatInterface {
    @Headers(
        "Content-Type: application/json"
    )
    @POST("completions")
    suspend fun sendMessage(
        @Header("Authorization") token: String,
        @Body message: ChatRequest
    ): ChatResponse
}
