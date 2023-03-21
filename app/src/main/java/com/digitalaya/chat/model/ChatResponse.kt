package com.digitalaya.chat.model

import com.digitalaya.chat.model.Message.Choice
import com.digitalaya.chat.model.Message.OpenAIError
import com.google.gson.annotations.SerializedName

data class ChatResponse(
    @SerializedName("model")
    val model: String,
    @SerializedName("choices")
    val Choices: List<Choice>,
    @SerializedName("error")
    val error: OpenAIError
)
