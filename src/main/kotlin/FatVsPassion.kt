import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.exceptions.TelegramApiException
import java.sql.DriverManager
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class FatVsPassion(private val token: String, private val chatName: String) : TelegramLongPollingBot() {

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
            val userId = message.from.id
            val chatId = message.chat.id
            val date = LocalDate.now()
            val userName = message.from.lastName + " " + message.from.firstName

            when (message.text) {
                "/start" -> {
                    val text = "안녕하세요! 지방은 열정을 이길 수 없다 Beta 입니다.\n" +
                            "/오운완 : 개인별 운동 기록\n" +
                            "/주간집계 : 일주일간 총 집계\n" +
                            "오늘도 고생하세욜 ㅋ"
                    sendTextMessage(chatId, text)
                }
                "/오운완" -> {
                    val insertQuery = "INSERT INTO mentions(user_id, user_name, date) VALUES (?, ?, ?)"
                    val statement = dbConnection.prepareStatement(insertQuery)
                    statement.setLong(1, userId.toLong())
                    statement.setString(2, userName)
                    statement.setString(3, date.format(dateFormatter))

                    statement.executeUpdate()

                    sendTextMessage(chatId, "입력완료!")

                    statement.close()
                }
                "/주간집계" -> {
                    val startOfWeek = date.with(DayOfWeek.MONDAY)
                    val endOfWeek = date.with(DayOfWeek.SUNDAY)
                    val selectQuery = "SELECT user_name, COUNT(user_id) as 'cnt' FROM mentions WHERE date >= ? AND date <= ? GROUP BY user_id"
                    val statement = dbConnection.prepareStatement(selectQuery)
                    statement.setString(1, startOfWeek.format(dateFormatter))
                    statement.setString(2, endOfWeek.format(dateFormatter))

                    val resultSet = statement.executeQuery()

                    var sendMsg = startOfWeek.format(dateFormatter) + " ~ " + endOfWeek.format(dateFormatter) + " 집계입니다.\n\n"
                    while(resultSet.next())
                        sendMsg += resultSet.getString("user_name") + " : " + resultSet.getInt("cnt") + " 회\n\n"

                    sendMsg += "\n한 주간 고생 많으셨습니다~"

                    sendTextMessage(chatId, sendMsg)

                    resultSet.close()
                    statement.close()
                }
                "/크크루삥뽕" -> {
                    val deleteQuery = "DELETE FROM mentions"
                    val statement = dbConnection.prepareStatement(deleteQuery)
                    statement.executeUpdate()

                    sendTextMessage(chatId, "컽")
                    
                    statement.close()
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

}