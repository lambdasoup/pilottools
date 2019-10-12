package com.lambdasoup.xpnet

import java.io.DataInputStream
import java.io.IOException
import java.net.DatagramPacket
import java.net.InetAddress
import java.net.MulticastSocket

fun main() {
    try {
        val udpSocket = MulticastSocket(49707)
        udpSocket.reuseAddress = true
        val group = InetAddress.getByName("239.255.1.1")
        udpSocket.joinGroup(group)
        val ba = ByteArray(1500)
        val packet = DatagramPacket(ba, ba.size)
        udpSocket.receive(packet)
        println("received packet from ${packet.address} with length ${packet.length}")

        val msg = Message.wrap(ba, packet.length)

        println(msg.base64String())


        val beacon = Parser().parse(msg)

        println("Received data $beacon")
    } catch (e: IOException) {
        println("UDP client has IOException/error: $e")
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

fun DataInputStream.readUnsignedInt(): Long = this.readInt().toUnsignedLong()
fun Int.toUnsignedLong(): Long = Integer.toUnsignedLong(this)
