import java.net.UnknownHostException
import java.net.ConnectException

fun main() {
    try {
        Client().use {
            it.connect("npm.mipt.ru", 9048)
            it.receiveHello()
            it.sendMessage("""HELLO\n""")
            val msgSum = it.receiveRes().sum()
            it.sendMessage("SUM$msgSum\n")
            it.receiveOk()
        }
    } catch (e: UnknownHostException) {
        println("Host is unreachable. Please check your network connection.")
    }
     catch (e: ConnectException) {
        println("Connection timed out. Please check if the desired port is available.")
    }
}