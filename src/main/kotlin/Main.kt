import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.meta.exceptions.TelegramApiException
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession

fun main(args: Array<String>) {
    //FatVsPassion_bot
    //5737640597
    val botName = "FatVsPassion_bot"
    val botToken = "5936746438:AAGFg3PoalQoHQ5UoAi-yieB61lgYlPfqnk"
    val botChaId = 5737640597
    val bot = FatVsPassion(botToken, botName)
    val telegramBotsApi = TelegramBotsApi(DefaultBotSession::class.java)
    telegramBotsApi.registerBot(bot)
}