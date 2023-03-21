package com.digitalaya.chat.model.Message

data class OpenAIError(
    val error: Error
)

data class Error(
    val message: String,
    val type: String,
    val param: String?,
    val code: String
)