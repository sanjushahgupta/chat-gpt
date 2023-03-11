package com.digitalaya.chat.model

import com.digitalaya.chat.model.Message.Message
import com.google.gson.annotations.SerializedName

data class ChatRequest(
    @SerializedName("model")
    var model : String = "gpt-3.5-turbo",
    @SerializedName("messages")
    val messages: List<Message>,
    @SerializedName("temperature")
    var temperature: Double = 7.5,
    )
