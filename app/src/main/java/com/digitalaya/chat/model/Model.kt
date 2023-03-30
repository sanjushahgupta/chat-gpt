package com.digitalaya.chat.model

import com.google.gson.annotations.SerializedName

//request
data class ChatRequest(
    @SerializedName("model")
    var model : String = "gpt-3.5-turbo",
    @SerializedName("messages")
    val messages: List<Message>,
    @SerializedName("temperature")
    var temperature: Double = 7.5,
    )
data class Message(
    @SerializedName("role")
    val role: String,
    @SerializedName("content")
    val content: String
)


//Response
data class ChatResponse(
    @SerializedName("model")
    val model: String,
    @SerializedName("choices")
    val Choices: List<Choice>,
    @SerializedName("error")
    val error: OpenAIError
)
data class Choice(
    var message: Message,
    var finish_reason: String,
    var index: Int
)



//error
data class Error(
    val message: String,
    val type: String,
    val param: String?,
    val code: String
)

data class OpenAIError(
    val error: Error
)
