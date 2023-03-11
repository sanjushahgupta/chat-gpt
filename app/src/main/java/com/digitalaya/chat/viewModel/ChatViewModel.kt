package com.digitalaya.chat.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.digitalaya.chat.RetrofitObj.chatGptAPi
import com.digitalaya.chat.model.ChatRequest
import com.digitalaya.chat.model.ChatResponse
import com.digitalaya.chat.model.Message.Message
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Retrofit

class ChatViewModel: ViewModel() {

    val myLiveData = MutableLiveData<String>()

    fun sendMessage() {
        viewModelScope.launch {
            try {

                val chatRequest = ChatRequest(
                    model = "gpt-3.5-turbo",
                    messages = listOf(Message(role = "user", content = "Say this is a test!")),
                    temperature = 0.7
                )
                val result = withContext(Dispatchers.IO) {
                    chatGptAPi.sendMessage(message = chatRequest).model
                }
                myLiveData.value = result
                Log.d("result is", myLiveData.value.toString())
            } catch (e: HttpException) {
                val error = when (e.code()) {
                    401 -> "Unauthorized"
                    404 -> "Not found"
                    else -> e.code().toString()
                }
                myLiveData.value = error
                Log.d("error is", e.code().toString())
            }
        }
    }

}
