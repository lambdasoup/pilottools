package com.lambdasoup.xpnet

import java.io.DataInputStream
import java.io.IOException
import java.net.DatagramPacket
import java.net.InetAddress
import java.net.MulticastSocket
import java.nio.ByteBuffer
import java.nio.charset.StandardCharsets
import java.util.*

fun main() {
    try {
        val udpSocket = MulticastSocket(49707)
        udpSocket.reuseAddress = true
        val group = InetAddress.getByName("239.255.1.1")
        udpSocket.joinGroup(group)
        val message = ByteArray(1500)
        val packet = DatagramPacket(message, message.size)
        udpSocket.receive(packet)
        println("received packet from ${packet.address} with length ${packet.length}")
        val bb = ByteBuffer.wrap(message, 0, packet.length)

        val bb64 = Base64.getEncoder().encode(bb)
        val s64 = StandardCharsets.US_ASCII.decode(bb64)
        println(s64)

        bb.rewind()

        val beacon = Parser().parse(bb)

        println("Received data $beacon")
    } catch (e: IOException) {
        println("UDP client has IOException/error: $e")
    }
}

data class Beacon(
    val majorVersion: Byte,
    val minorVersion: Int,
    val applicationHostId: Int,
    val versionNumber: Int,
    val role: Long,
    val port: Int,
    val computerName: String
)

fun DataInputStream.readUnsignedInt(): Long = this.readInt().toUnsignedLong()
fun Int.toUnsignedLong(): Long = Integer.toUnsignedLong(this)
