package com.digitalaya.chat.model

import android.os.Message
import com.google.gson.annotations.SerializedName

data class ChatResponse(
        @SerializedName("model")
        val model: String,

)
