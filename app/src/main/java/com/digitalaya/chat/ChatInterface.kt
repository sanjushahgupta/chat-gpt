package com.digitalaya.chat

import com.digitalaya.chat.model.ChatRequest
import com.digitalaya.chat.model.ChatResponse
import com.digitalaya.chat.model.Message.Message
import retrofit2.http.*

interface ChatInterface {
    @Headers(
        "Content-Type: application/json",
        "Authorization: Bearer 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",
    )
    @POST("completions")
    suspend fun sendMessage(
        @Body message: ChatRequest
    ): ChatResponse



}