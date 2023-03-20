package com.whalee

import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession

fun main(args: Array<String>) {
    val botName = "FatVsPassion_bot"
    val botToken = "5936746438:AAGFg3PoalQoHQ5UoAi-yieB61lgYlPfqnk"
    //val botChaId = 5737640597
    val bot = FatVsPassionUtil(botToken, botName)
    val telegramBotsApi = TelegramBotsApi(DefaultBotSession::class.java)
    telegramBotsApi.registerBot(bot)
}