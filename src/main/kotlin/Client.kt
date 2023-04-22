import java.io.DataInputStream
import java.io.PrintWriter
import java.net.Socket

/**
 * Connection interface
 *
 * This class provides connection to desired server, reading and sending messages to it.
 *
 * @property socket creates socket
 * @property sender creates message sender to server
 * @property receiver creates message receiver from server
 */
class Client() {
    private lateinit var socket: Socket
    private lateinit var sender: PrintWriter
    private lateinit var receiver: DataInputStream

    /**
     * Creates connection to desired server destination
     *
     * @param address server address
     * @param port server port
     */
    fun connect(address: String = "127.0.0.1", port: Int = 9999) {
        socket = Socket(address, port)
        sender = PrintWriter(socket.getOutputStream(), true)
        receiver = DataInputStream(socket.inputStream)
        println("Successful connection : ${socket.inetAddress.hostAddress}.")
    }

    /**
     * Closes connection to the server
     */
    fun endConnection() {
        socket.close()
        receiver.close()
        sender.close()
        println("Connection ended by client.")
    }

    /**
     * Sends string message to binded server
     *
     * @param message message to be sent
     */
    fun sendMessage(message: String = "") {
        sender.println(message)
        sender.flush()
        println("Message $message was sent to the server ${socket.inetAddress.hostAddress}.")
    }

    /**
     * Receives HELLO\n from server
     */
    fun receiveHello() = println("Server replied: ${receiver.readNBytes(6).toString(Charsets.US_ASCII)}") // HELLO consists of 6 bytes

    /**
     * Receives OK<some garbage> from server until \n is reached
     */
    fun receiveOk() {
        println("Server replied: ${receiver.readNBytes(2).toString(Charsets.US_ASCII)}") // Receive OK
        var responseChar: Byte = receiver.readByte()
        while (responseChar.toInt() != '\n'.code) { // If server sends some garbage
            print(responseChar)
            responseChar = receiver.readByte()
        }
    }

    /**
     * Receives RES string from the server
     *
     * @return list with received bytes from RES response
     */
    fun receiveRes(): List<UByte> {
        receiver.readNBytes(3) // Reads RES from server. No meaningful info here, so it's not assigned to anything
        val msgLength = receiver.readByte().toInt() // Reads message length
        val msg = receiver.readNBytes(msgLength).map { it.toUByte() }
        receiver.readByte() // \n symbol encodes with 2 bytes so response from server contains extra zero, we read it here
        if (receiver.readByte().toInt() != '\n'.code) println("""\n was not found in the end of message, something's wrong!""")
        return msg
    }
}