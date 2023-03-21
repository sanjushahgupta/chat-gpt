package com.digitalaya.chat.model.Message

import com.digitalaya.chat.model.Message.Message

data class Choice(
    var message: Message,
    var finish_reason: String,
    var index: Int
)
