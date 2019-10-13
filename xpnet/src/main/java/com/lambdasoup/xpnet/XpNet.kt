package com.lambdasoup.xpnet

import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.net.MulticastSocket
import java.nio.ByteBuffer

fun main() {
    val client = getClient()

    val connection = Connection(client)

    connection.sendCommand("sim/electrical/battery_1_toggle")
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

fun getClient(): Client {
    val udpSocket = MulticastSocket(49707)
    udpSocket.reuseAddress = true
    val group = InetAddress.getByName("239.255.1.1")
    udpSocket.joinGroup(group)
    val ba = ByteArray(1500)
    val packet = DatagramPacket(ba, ba.size)
    udpSocket.receive(packet)
    val msg = Message.wrap(ba, packet.length)


    val beacon = Parser().parse(msg)
    return Client(
        name = beacon.computerName,
        address = packet.address
    )
}

data class Command(
    val commandStr: String
)

data class Beacon(
    val majorVersion: Byte,
    val minorVersion: Byte,
    val applicationHostId: Int,
    val versionNumber: Int,
    val role: UInt,
    val port: UShort,
    val computerName: String
)
