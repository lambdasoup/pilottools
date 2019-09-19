package com.lambdasoup.xpnet

import java.io.IOException
import java.net.DatagramPacket
import java.net.DatagramSocket
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
            val text = String(message, 0, packet.getLength())
            print("Received data $text")
        } catch (e: IOException) {
            print("UDP client has IOException/error: $e")
        }
}
