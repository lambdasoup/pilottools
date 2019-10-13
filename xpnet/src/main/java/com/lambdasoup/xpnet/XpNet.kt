package com.lambdasoup.xpnet

import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.net.MulticastSocket
import java.nio.ByteBuffer
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit

fun main() {
    // get first seen client
    val reg = Registry()
    val future = CompletableFuture<Client>()
    val listener = Thread {
        reg.listen { client ->
            future.complete(client)
        }
    }
    listener.isDaemon = true
    listener.start()
    val client = future.get(5, TimeUnit.SECONDS)

    //
    val connection = Connection(client)

    connection.sendCommand("sim/electrical/battery_1_toggle")

    // TODO request DREF
    // TODO read RREFS
    // TODO stop RREFS (request DREFS with freq 0)
}

class Connection(private val client: Client) {

    private val socket = DatagramSocket()
    private val sendBuf = ByteBuffer.allocate(500)

    fun sendCommand(command: String) {
        sendBuf.rewind()
        val ba = "CMND\u0000$command\u0000".toByteArray(charset = Charsets.US_ASCII)
        val packet = DatagramPacket(ba, 0, ba.size, client.address, 49000)
        socket.send(packet)
    }
}


data class Client(
    val name: String,
    val address: InetAddress
)

class Registry {
    private val socket = MulticastSocket(49707)
    private val ba = ByteArray(1500)

    init {
        socket.reuseAddress = true
        val group = InetAddress.getByName("239.255.1.1")
        socket.joinGroup(group)

    }

    fun listen(listener: (Client) -> Unit) {
        while (true) {
            val packet = DatagramPacket(ba, ba.size)
            socket.receive(packet)
            val msg = Message.wrap(ba, packet.length)

            val beacon = Parser().parse(msg)
            listener(
                Client(
                    name = beacon.computerName,
                    address = packet.address
                )
            )
        }
    }
}

data class Beacon(
    val majorVersion: Byte,
    val minorVersion: Byte,
    val applicationHostId: Int,
    val versionNumber: Int,
    val role: UInt,
    val port: UShort,
    val computerName: String
)
