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

    // create connection
    val connection = Connection(client)

    // request DREF
    println("request rrefs")
    connection.requestRrefs(10, "sim/cockpit/autopilot/heading_mag")

    // subscribe some RREFS
    println("receive rrefs")
    val drefListener = Thread {
        connection.listen { value ->
            println(value)
        }
    }
    drefListener.isDaemon = true
    drefListener.start()

    // send test command
    connection.sendCommand("sim/electrical/battery_1_on")
    repeat(100) {
        connection.sendCommand("sim/autopilot/heading_up")
        Thread.sleep(100)
    }

    // stop RREFS (request DREFS with freq 0)
    println("stopping rrefs")
    connection.stopRrefs()
    Thread.sleep(2000)
}

class Connection(private val client: Client) {

    private val socket = DatagramSocket()
    private val sendBuf = ByteBuffer.allocate(413).apply { order(ByteOrder.LITTLE_ENDIAN) }
    private val receiveBuf = ByteBuffer.allocate(1500).apply { order(ByteOrder.LITTLE_ENDIAN) }

    private val rrefs = arrayListOf<String>()

    fun sendCommand(command: String) {
        sendBuf.clear()
        sendBuf.put("CMND\u0000".toByteArray(charset = Charsets.US_ASCII))
        sendBuf.put(command.toByteArray(charset = Charsets.US_ASCII))
        sendBuf.put(0x00)
        val packet = DatagramPacket(sendBuf.array(), 0, sendBuf.capacity(), client.address, 49000)
        socket.send(packet)
    }

    fun requestRrefs(freq: Int, dref: String) {
        val id = rrefs.size
        rrefs.add(dref)

        sendRrefReq(freq, id, dref)
    }

    private fun sendRrefReq(freq: Int, id: Int, dref: String) {
        sendBuf.clear()
        sendBuf.put("RREF\u0000".toByteArray(charset = Charsets.US_ASCII))
        sendBuf.putInt(freq)
        sendBuf.putInt(id)
        sendBuf.put(dref.toByteArray(charset = Charsets.US_ASCII))
        sendBuf.put(0x00)
        val packet = DatagramPacket(sendBuf.array(), 0, sendBuf.capacity(), client.address, 49000)
        socket.send(packet)
    }

    fun listen(listener: (Value) -> Unit) {
        while (true) {
            receiveBuf.clear()
            val packet = DatagramPacket(
                receiveBuf.array(), 0, receiveBuf.capacity(),
                client.address, 49000
            )
            socket.receive(packet)
            val msg = Message.wrap(receiveBuf.array(), packet.length)
            listener(
                Value(
                    dref = rrefs[msg.xint()],
                    value = msg.xflt()
                )
            )
        }
    }

    fun stopRrefs() {
        rrefs.forEachIndexed { id, dref -> sendRrefReq(0, id, dref) }
    }
}


data class Client(
    val name: String,
    val address: InetAddress
)

data class Value(
    val dref: String,
    val value: Float
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
