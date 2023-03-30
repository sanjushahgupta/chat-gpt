package com.digitalaya.chat.viewModel

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.digitalaya.chat.chatRepo.ChatRepository


import kotlinx.coroutines.*



class ChatViewModel(private val chatRepository: ChatRepository) : ViewModel() {

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
            val answer = chatRepository.makeRequestToOpenAI(ctx,userQuestion, storeApiKey)
            liveData.add(index, answer)
            listOfLiveData.postValue(liveData)
            index += 1
            loadingBoolean.postValue(false)
        }
    }
}
