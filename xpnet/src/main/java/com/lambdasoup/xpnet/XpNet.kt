package com.lambdasoup.xpnet

import java.io.*
import java.net.DatagramPacket
import java.net.InetAddress
import java.net.MulticastSocket

fun main() {
        try {
            val udpSocket = MulticastSocket(49707)
            udpSocket.reuseAddress = true
            val group = InetAddress.getByName("239.255.1.1")
            udpSocket.joinGroup(group)
            val message = ByteArray(8000)
            val packet = DatagramPacket(message, message.size)
            print("UDP client: about to wait to receive")
            udpSocket.receive(packet)
            val text = String(message, 0, packet.length)
            val bis = DataInputStream(ByteArrayInputStream(message))
            val ba = ByteArray(5)
            val prologue = bis.read(ba)
            print("Received data ${String(ba)}")

            val beacon = Beacon(
                majorVersion = bis.readUnsignedByte(),
                minorVersion = bis.readUnsignedByte(),
                applicationHostId = bis.readInt(),
                versionNumber = bis.readInt(),
                role = bis.readUnsignedInt(),
                port = bis.readUnsignedShort(),
                computerName = run {
                    val ba = ByteArray(500)
                    bis.read(ba)
                    String(ba).trim()
                }
            )
            print("Received data $beacon")
        } catch (e: IOException) {
            print("UDP client has IOException/error: $e")
        }
}

data class Beacon(
    val majorVersion: Int,
    val minorVersion: Int,
    val applicationHostId: Int,
    val versionNumber: Int,
    val role: Long,
    val port: Int,
    val computerName: String
)

fun DataInputStream.readUnsignedInt(): Long = this.readInt().toUnsignedLong()
fun Int.toUnsignedLong(): Long = Integer.toUnsignedLong(this)
