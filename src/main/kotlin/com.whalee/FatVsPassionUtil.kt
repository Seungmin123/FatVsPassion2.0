package com.whalee

import com.whalee.dto.UserInfo
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.exceptions.TelegramApiException
import java.sql.DriverManager
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class FatVsPassionUtil(private val token: String, private val chatName: String) : TelegramLongPollingBot() {

    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    private val dbConnection = DriverManager.getConnection("jdbc:sqlite:mentions.db")

    init {
        dbConnection.createStatement().execute(
            "CREATE TABLE IF NOT EXISTS mentions (" +
                    "mention_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "user_id INTEGER, " +
                    "user_name TEXT, " +
                    "date TEXT)"
        )
    }

    override fun getBotUsername(): String = chatName

    override fun getBotToken(): String = token

    override fun onUpdateReceived(update: Update) {
        if (update.hasMessage() && update.message.hasText()) {
            val message = update.message
            val userName: String? = "${message.from.lastName ?: ""} ${message.from.firstName ?: ""}"
            val userInfo = UserInfo(message.from.id, userName, LocalDate.now(), message.chat.id)

            when (message.text) {
                "/start" -> {
                    selectIndexMessage(userInfo.chatId)
                }
                "/명령어" -> {
                    selectMethodList(userInfo.chatId)
                }
                "/오운완" -> {
                    exerciseInsert(userInfo)
                }
                "/주간집계" -> {
                    selectWeeklyCntMsg(userInfo)
                }
                "/삭제" -> {
                    deleteUserRecentData(userInfo)
                }
                "/크크루삥뽕" -> {
                    deleteAllData(userInfo.chatId)
                }
            }
        }
    }

    fun sendTextMessage(chatId: Long, text: String) {
        // 메시지 전송
        val message = SendMessage(chatId.toString(), text)
        try {
            execute<Message, SendMessage>(message)
        } catch (e: TelegramApiException) {}
    }

    fun selectIndexMessage(chatId: Long){
        val message = "안녕하세요! 지방은 열정을 이길 수 없다 Ver_1.1 입니다.\n" +
                "/명령어 : 명령어 모음"
        sendTextMessage(chatId, message)
    }

    fun selectMethodList(chatId: Long) {
        val message = "명령어 모음 입니다.\n"
        "/오운완 : 개인별 운동 기록\n" +
                "/주간집계 : 일주일간 총 집계\n" +
                "/삭제 : 최근 입력 데이터 삭제"
        sendTextMessage(chatId, message)
    }

    fun exerciseInsert(userInfo: UserInfo) {
        val insertQuery = "INSERT INTO mentions(user_id, user_name, date) VALUES (?, ?, ?)"
        val statement = dbConnection.prepareStatement(insertQuery)
        statement.setLong(1, userInfo.userId.toLong())
        statement.setString(2, userInfo.userName)
        statement.setString(3, userInfo.date.format(dateFormatter))

        statement.executeUpdate()

        statement.close()

        sendTextMessage(userInfo.chatId, "${userInfo.userName ?: ""} 입력완료!")
    }

    fun selectWeeklyCntMsg(userInfo: UserInfo) {
        val startOfWeek = userInfo.date.with(DayOfWeek.MONDAY)
        val endOfWeek = userInfo.date.with(DayOfWeek.SUNDAY)
        val selectQuery = "SELECT user_name, COUNT(user_id) as 'cnt' FROM mentions WHERE date >= ? AND date <= ? GROUP BY user_id"
        val statement = dbConnection.prepareStatement(selectQuery)
        statement.setString(1, startOfWeek.format(dateFormatter))
        statement.setString(2, endOfWeek.format(dateFormatter))

        val resultSet = statement.executeQuery()

        var sendMsg = startOfWeek.format(dateFormatter) + " ~ " + endOfWeek.format(dateFormatter) + " 집계입니다.\n\n"
        while(resultSet.next())
            sendMsg += resultSet.getString("user_name") + " : " + resultSet.getInt("cnt") + " 회\n\n"

        sendMsg += "\n한 주간 고생 많으셨습니다!"

        resultSet.close()
        statement.close()

        sendTextMessage(userInfo.chatId, sendMsg)
    }

    fun deleteUserRecentData(userInfo: UserInfo) {
        val deleteQuery = "DELETE FROM mentions " +
                "WHERE user_id = ? " +
                "  AND date = (" +
                "   SELECT MAX(date)" +
                "   FROM mentions" +
                "   WHERE user_id = ?" +
                "   GROUP BY user_id" +
                ") V"
        val statement = dbConnection.prepareStatement(deleteQuery)
        statement.setString(1, userInfo.userId.toString())
        statement.setString(2, userInfo.userId.toString())

        statement.executeUpdate()

        statement.close()

        sendTextMessage(userInfo.chatId, "${userInfo.userName} 님 최근 데이터를 삭제하였습니다.")
    }

    fun deleteAllData(chatId: Long){
        val deleteQuery = "DELETE FROM mentions"
        val statement = dbConnection.prepareStatement(deleteQuery)
        statement.executeUpdate()

        statement.close()

        sendTextMessage(chatId, "데이터 삭제 완료")
    }


}