package com.lambdasoup.xpnet

import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.net.MulticastSocket
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit

fun main() {
    // get first seen client
    val reg = Registry()
    val future = CompletableFuture<Client>()
    val clientListener = Thread {
        reg.listen { client ->
            future.complete(client)
        }
    }
    clientListener.isDaemon = true
    clientListener.start()
    val client = future.get(5, TimeUnit.SECONDS)

    // send test command
    val connection = Connection(client)
    connection.sendCommand("sim/electrical/battery_1_toggle")

    // request DREF
    connection.requestRrefs(1, 123, "sim/cockpit2/controls/flap_ratio")

    // TODO read RREFS
    val drefListener = Thread {
        connection.listen { packet ->
            println(packet)
        }
    }
    drefListener.isDaemon = true
    drefListener.start()
    Thread.sleep(2000)
    println("stop")

    // stop RREFS (request DREFS with freq 0)
    connection.requestRrefs(0, 123, "sim/cockpit2/controls/flap_ratio")
}

class Connection(private val client: Client) {

    private val socket = DatagramSocket()
    private val sendBuf = ByteBuffer.allocate(500).apply { order(ByteOrder.LITTLE_ENDIAN) }
    private val receiveBuf = ByteBuffer.allocate(1500).apply { order(ByteOrder.LITTLE_ENDIAN) }

    fun sendCommand(command: String) {
        sendBuf.rewind()
        val ba = "CMND\u0000$command\u0000".toByteArray(charset = Charsets.US_ASCII)
        val packet = DatagramPacket(ba, 0, ba.size, client.address, 49000)
        socket.send(packet)
    }

    fun requestRrefs(freq: Int, id: Int, dref: String) {
        sendBuf.rewind()
        sendBuf.put("RREF\u0000".toByteArray(charset = Charsets.US_ASCII))
        sendBuf.putInt(freq)
        sendBuf.putInt(id)
        sendBuf.put(dref.toByteArray(charset = Charsets.US_ASCII))
        val packet = DatagramPacket(sendBuf.array(), 0, sendBuf.position(), client.address, 49000)
        socket.send(packet)
    }

    fun listen(listener: (DatagramPacket) -> Unit) {
        while (true) {
            val packet = DatagramPacket(
                receiveBuf.array(), 0, receiveBuf.capacity(),
                client.address, 49000
            )
            socket.receive(packet)
            listener(packet)
        }
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
