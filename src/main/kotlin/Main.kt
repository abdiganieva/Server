import java.net.UnknownHostException
import java.net.ConnectException

fun main() {
    try {
        val client = Client()
        client.connect("npm.mipt.ru", 9048)
        //client.sendMessage("""HELLO\n""")
        client.receiveHello()
        client.sendMessage("""HELLO\n""")
        val msgSum = client.receiveRes().sum()
        client.sendMessage("SUM$msgSum\n")
        client.receiveOk()
        client.endConnection()
    } catch (e: UnknownHostException) {
        println("Host is unreachable. Please check your network connection.")
    }
     catch (e: ConnectException) {
        println("Connection timed out. Please check if the desired port is available.")
    }
}