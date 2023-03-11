package com.digitalaya.chat

import com.digitalaya.chat.model.ChatResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object RetrofitObj {
    private const val BaseUrl ="https://api.openai.com/v1/chat/"
    private val retrofit: Retrofit = Retrofit.Builder().baseUrl(BaseUrl).addConverterFactory(GsonConverterFactory.create()).build()
    val chatGptAPi:ChatInterface = retrofit.create(ChatInterface::class.java)
}