package com.digitalaya.chat.viewModel

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.digitalaya.chat.RetrofitObj.openAIAPI
import com.digitalaya.chat.model.ChatRequest
import com.digitalaya.chat.model.ChatResponse
import com.digitalaya.chat.model.Message.Message
import com.digitalaya.chat.model.Message.OpenAIError
import com.google.gson.Gson

import kotlinx.coroutines.*
import retrofit2.HttpException


class ChatViewModel : ViewModel() {
    private val liveData = mutableListOf("")
    val listOfLiveData = MutableLiveData<MutableList<String>>()
    val loadingBoolean = MutableLiveData<Boolean>()
    var index = 0

    @SuppressLint("SuspiciousIndentation")
    fun sendMessage(ctx: Context, userQuestion: String, storeApiKey: String) {
        loadingBoolean.postValue(true)
        liveData.add(index, userQuestion)
        index += 1

        viewModelScope.launch(Dispatchers.IO) {
            val answer = makeRequestToOpenAI(ctx, userQuestion, storeApiKey)
            liveData.add(index, answer)
            listOfLiveData.postValue(liveData)
            index += 1
            loadingBoolean.postValue(false)
        }
    }

    private suspend fun makeRequestToOpenAI(
        ctx: Context,
        userQuestion: String,
        storeApiKey: String
    ): String {
        if (!isNetworkAvailable(ctx)) {
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
                resp = openAIAPI.sendMessage(
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
            return error.error.message
        } catch (e: TimeoutCancellationException) {
            return e.message.toString()
        }

        return resp.Choices[0].message.content
    }

    private fun isNetworkAvailable(context: Context): Boolean {
        try {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkCapabilities = connectivityManager.activeNetwork ?: return false
            val networkInfo =
                connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false

            return networkInfo.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || networkInfo.hasTransport(
                NetworkCapabilities.TRANSPORT_CELLULAR
            )
        } catch (e: Exception) {
            return false
        }
    }
}
