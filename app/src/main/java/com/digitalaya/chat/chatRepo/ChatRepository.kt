package com.digitalaya.chat.chatRepo

import android.content.Context
import com.digitalaya.chat.ChatInterface
import com.digitalaya.chat.model.ChatRequest
import com.digitalaya.chat.model.ChatResponse
import com.digitalaya.chat.model.Message
import com.digitalaya.chat.model.OpenAIError
import com.digitalaya.chat.util.NetworkUtil
import com.google.gson.Gson
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withTimeout
import retrofit2.HttpException

class ChatRepository {
suspend fun makeRequestToOpenAI(
ctx: Context,
        userQuestion: String,
        storeApiKey: String
    ): String {

    if (!NetworkUtil.isNetworkAvailable(ctx)) {
        return "No internet available, please check your connection."
    }

    val chatRequest = ChatRequest(
            model = "gpt-3.5-turbo",
            messages = listOf(Message(role = "user", content = userQuestion)),
            temperature = 0.7
        )

        val resp: ChatResponse

        try {
            withTimeout(30 * 1000) {
                resp = ChatInterface.RetrofitObj.openAIAPI.sendMessage(
                    token = "Bearer $storeApiKey",
                    message = chatRequest
                )
            }
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            if (errorBody == "") {
                return e.message.toString()
            }

            val error = Gson().fromJson(errorBody, OpenAIError::class.java)
            if (error.error.code == "invalid_api_key") {
                return "Your key appears to be invalid. Please update it using the key icon on top right."
            }
            return error.error.type.toString()
        } catch (e: TimeoutCancellationException) {
            return e.message.toString()
        }

        return resp.Choices[0].message.content
    }
}