package com.whalee.dto

import java.time.LocalDate

class UserInfo (userId: Long, userName: String?, date: LocalDate, chatId: Long){
    var userId = userId
    var userName = userName ?: ""
    var date = date
    var chatId = chatId
}